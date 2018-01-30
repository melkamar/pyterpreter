/*
 * Copyright (c) 2014 by Bart Kiers
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package cz.melkamar.pyterpreter.parser;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.antlr.Python3Lexer;
import cz.melkamar.pyterpreter.antlr.Python3Parser;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.exceptions.ParseException;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.nodes.PySuiteNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * A small class that flattens an ANTLR4 {@code SimpleParseTree}. Given the
 * {@code SimpleParseTree}:
 * <p>
 * <pre>
 * <code>
 * a
 * '-- b
 * |   |
 * |   '-- d
 * |       |
 * |       '-- e
 * |           |
 * |           '-- f
 * |
 * '-- c
 * </code>
 * </pre>
 * <p>
 * This class will flatten this structure as follows:
 * <p>
 * <pre>
 * <code>
 * a
 * '-- b
 * |   |
 * |   '-- f
 * |
 * '-- c
 * </code>
 * </pre>
 * <p>
 * In other word: all inner nodes that have a single child are removed from the SimpleParseTree.
 */
@SuppressWarnings("Duplicates")
public class SimpleParseTree {

    final static int NODE_TYPE_TERM = 1;

    /**
     * The payload will either be the name of the parser rule, or the token
     * of a leaf in the tree.
     */
    private final Object payload;

    /**
     * All child nodes of this SimpleParseTree.
     */
    private final List<SimpleParseTree> children;

    public SimpleParseTree(org.antlr.v4.runtime.tree.ParseTree tree) {
        this(null, tree);
    }

    private SimpleParseTree(SimpleParseTree simpleParseTree, org.antlr.v4.runtime.tree.ParseTree tree) {
        this(simpleParseTree, tree, new ArrayList<SimpleParseTree>());
    }

    private SimpleParseTree(SimpleParseTree parent,
                            org.antlr.v4.runtime.tree.ParseTree tree,
                            List<SimpleParseTree> children) {

        this.payload = getPayload(tree);
        this.children = children;

        if (parent == null) {
            // We're at the root of the SimpleParseTree, traverse down the parse tree to fill
            // this SimpleParseTree with nodes.
            walk(tree, this);
        } else {
            parent.children.add(this);
        }
    }

    public Object getPayload() {
        return payload;
    }

    public String getPayloadAsString() {
        return String.valueOf(payload);
    }

    public List<SimpleParseTree> getChildren() {
        return new ArrayList<>(children);
    }

    public SimpleParseTree getChild(int index) {
        return children.get(index);
    }

    public Object getChildPayload(int index) {
        return children.get(index).payload;
    }

    public int getChildCount() {
        return children.size();
    }

    // Determines the payload of this SimpleParseTree: a string in case it's an inner node (which
    // is the name of the parser rule), or a Token in case it is a leaf node.
    private Object getPayload(org.antlr.v4.runtime.tree.ParseTree tree) {
        if (tree.getChildCount() == 0) {
            // A leaf node: return the tree's payload, which is a Token.
            return tree.getPayload();
        } else {
            // The name for parser rule `foo` will be `FooContext`. Strip `Context` and
            // lower case the first character.
            String ruleName = tree.getClass().getSimpleName().replace("Context", "");
            return Character.toLowerCase(ruleName.charAt(0)) + ruleName.substring(1);
        }
    }

    // Fills this SimpleParseTree based on the parse tree.
    private static void walk(org.antlr.v4.runtime.tree.ParseTree tree, SimpleParseTree simpleParseTree) {

        if (tree.getChildCount() == 0) {
            // We've reached a leaf. We must create a new instance of an SimpleParseTree because
            // the constructor will make sure this new instance is added to its parent's
            // child nodes.
            new SimpleParseTree(simpleParseTree, tree);
        } else if (tree.getChildCount() == 1) {
            // We've reached an inner node with a single child: we don't include this in
            // our SimpleParseTree.
            walk(tree.getChild(0), simpleParseTree);
        } else if (tree.getChildCount() > 1) {

            for (int i = 0; i < tree.getChildCount(); i++) {

                SimpleParseTree temp = new SimpleParseTree(simpleParseTree, tree.getChild(i));

                if (!(temp.payload instanceof Token)) {
                    // Only traverse down if the payload is not a Token.
                    walk(tree.getChild(i), temp);
                }
            }
        }
    }

    public static SimpleParseTree genParseTree(String code) {
        // parser throws errors if there is no newline at the end of file - add it there
        if (!code.endsWith(System.lineSeparator())) {
            code += System.lineSeparator();
        }

        Python3Lexer lexer = new Python3Lexer(new ANTLRInputStream(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);

        org.antlr.v4.runtime.tree.ParseTree parseTree = parser.file_input();
        int errors = parser.getNumberOfSyntaxErrors();
        if (errors > 0) {
            throw new ParseException(errors + " errors while parsing.");
        }

        return new SimpleParseTree(parseTree);
    }

    /**
     * Parse given code into AST. Return root node.
     */
    public static PyRootNode astFromCode(String code) {
        SimpleParseTree ast = genParseTree(code);
        return ast.generateAST();
    }

    public static SimpleParseTree fromFile(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        String code = new String(encoded, Charset.forName("UTF-8"));

        return genParseTree(code);
    }

    /**
     * Parse code into a SimpleParseTree, print it - used for debugging.
     */
    public static void printParseTree(String code) {
        SimpleParseTree ast = genParseTree(code);
        System.out.println(ast);
    }

    /**
     * Generate AST from this SimpleParseTree.
     */
    public PyRootNode generateAST() {
        FrameDescriptor frameDescriptor = Environment.DEFAULT.getDefaultFrameDescriptor();
        PySuiteNode suiteNode = SptToAstTransformer.parseFileInputBlock(this, frameDescriptor);
        PyRootNode rootPyNode = new PyRootNode(suiteNode, frameDescriptor);
        return rootPyNode;
    }


    public int getNontokenType() {
        switch (String.valueOf(this.payload)) {
            case "term":
                return NODE_TYPE_TERM;
            default:
                throw new NotImplementedException();
        }
    }

    /**
     * Check if current SimpleParseTree node is an instance of Token.
     */
    boolean isToken() {
        return this.payload instanceof Token;
    }

    /**
     * Check if a child at a given position is an instance of Token.
     *
     * @param index Index of the child in ast.
     * @throws IndexOutOfBoundsException When child with such index does not exist.
     */
    public boolean isChildToken(int index) {
        return this.children.get(index).payload instanceof Token;
    }

    /**
     * Get a node's child and cast as Token.
     */
    Token childAsToken(int index) {
        return (Token) this.children.get(index).payload;
    }

    /**
     * Cast this to Token.
     *
     * @return
     */
    Token asToken() {
        return (Token) this.payload;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        SimpleParseTree simpleParseTree = this;
        List<SimpleParseTree> firstStack = new ArrayList<>();
        firstStack.add(simpleParseTree);

        List<List<SimpleParseTree>> childListStack = new ArrayList<>();
        childListStack.add(firstStack);

        while (!childListStack.isEmpty()) {

            List<SimpleParseTree> childStack = childListStack.get(childListStack.size() - 1);

            if (childStack.isEmpty()) {
                childListStack.remove(childListStack.size() - 1);
            } else {
                simpleParseTree = childStack.remove(0);
                String caption;

                if (simpleParseTree.payload instanceof Token) {
                    Token token = (Token) simpleParseTree.payload;
                    String tokenEscaped = token.getText().replace("\n", "\\n").replace("\r", "\\r");
                    caption = String.format("TOKEN[type: %s, text: %s]",
                                            token.getType(), tokenEscaped);
//                    System.out.println("got token: " + tokenEscaped + " (" + token.getType() + ")");
                } else {
                    caption = String.valueOf(simpleParseTree.payload);
//                    System.out.println("got non-token: " + caption);
                }

                String indent = "";

                for (int i = 0; i < childListStack.size() - 1; i++) {
                    indent += (childListStack.get(i).size() > 0) ? "|  " : "   ";
                }

                builder.append(indent)
                        .append(childStack.isEmpty() ? "'- " : "|- ")
                        .append(caption)
                        .append("\n");

                if (simpleParseTree.children.size() > 0) {
                    List<SimpleParseTree> children = new ArrayList<>();
                    for (int i = 0; i < simpleParseTree.children.size(); i++) {
                        children.add(simpleParseTree.children.get(i));
                    }
                    childListStack.add(children);
                }
            }
        }

        return builder.toString();
    }
}