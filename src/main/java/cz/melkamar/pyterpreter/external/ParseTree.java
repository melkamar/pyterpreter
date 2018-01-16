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
package cz.melkamar.pyterpreter.external;

import cz.melkamar.pyterpreter.antlr.Python3Parser;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.nodes.arithmetic.PyAddNode;
import cz.melkamar.pyterpreter.nodes.PyFunctionNode;
import cz.melkamar.pyterpreter.nodes.PyNumberNode;
import cz.melkamar.pyterpreter.nodes.arithmetic.PySubtractNode;
import cz.melkamar.pyterpreter.nodes.template.PyNode;
import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import cz.melkamar.pyterpreter.antlr.Python3Lexer;

import java.util.*;

/**
 * A small class that flattens an ANTLR4 {@code ParseTree}. Given the
 * {@code ParseTree}:
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
 * In other word: all inner nodes that have a single child are removed from the ParseTree.
 */
@SuppressWarnings("Duplicates")
public class ParseTree {

    /**
     * The payload will either be the name of the parser rule, or the token
     * of a leaf in the tree.
     */
    private final Object payload;

    /**
     * All child nodes of this ParseTree.
     */
    private final List<ParseTree> children;

    public ParseTree(org.antlr.v4.runtime.tree.ParseTree tree) {
        this(null, tree);
    }

    private ParseTree(ParseTree parseTree, org.antlr.v4.runtime.tree.ParseTree tree) {
        this(parseTree, tree, new ArrayList<ParseTree>());
    }

    private ParseTree(ParseTree parent, org.antlr.v4.runtime.tree.ParseTree tree, List<ParseTree> children) {

        this.payload = getPayload(tree);
        this.children = children;

        if (parent == null) {
            // We're at the root of the ParseTree, traverse down the parse tree to fill
            // this ParseTree with nodes.
            walk(tree, this);
        } else {
            parent.children.add(this);
        }
    }

    public Object getPayload() {
        return payload;
    }

    public List<ParseTree> getChildren() {
        return new ArrayList<>(children);
    }

    // Determines the payload of this ParseTree: a string in case it's an inner node (which
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

    // Fills this ParseTree based on the parse tree.
    private static void walk(org.antlr.v4.runtime.tree.ParseTree tree, ParseTree parseTree) {

        if (tree.getChildCount() == 0) {
            // We've reached a leaf. We must create a new instance of an ParseTree because
            // the constructor will make sure this new instance is added to its parent's
            // child nodes.
            new ParseTree(parseTree, tree);
        } else if (tree.getChildCount() == 1) {
            // We've reached an inner node with a single child: we don't include this in
            // our ParseTree.
            walk(tree.getChild(0), parseTree);
        } else if (tree.getChildCount() > 1) {

            for (int i = 0; i < tree.getChildCount(); i++) {

                ParseTree temp = new ParseTree(parseTree, tree.getChild(i));

                if (!(temp.payload instanceof Token)) {
                    // Only traverse down if the payload is not a Token.
                    walk(tree.getChild(i), temp);
                }
            }
        }
    }

    /**
     * Parse code into ParseTree and return the root node.
     */
    public static PyRootNode astFromCode(String code) {
        Python3Lexer lexer = new Python3Lexer(new ANTLRInputStream(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);

        org.antlr.v4.runtime.tree.ParseTree parseTree = parser.file_input();
        ParseTree ast = new ParseTree(parseTree);
        PyRootNode rootNode = ast.generateAST();
        return rootNode;
    }

    /**
     * Parse code into a parsetree, print it - used for debugging.
     */
    public static void printParseTree(String code) {
        Python3Lexer lexer = new Python3Lexer(new ANTLRInputStream(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);

        org.antlr.v4.runtime.tree.ParseTree parseTree = parser.file_input();
        ParseTree ast = new ParseTree(parseTree);
        System.out.println(ast);
    }

    /**
     * Generate ParseTree from this parsetree.
     *
     * @return
     */
    public PyRootNode generateAST() {
        PyRootNode rootPyNode = new PyRootNode();
        traverse(this, rootPyNode);
        System.out.println(rootPyNode);
        rootPyNode.print();
        return rootPyNode;
    }

    private PyNode parseFuncDef(ParseTree parseTree, PyNode currentPyNode) {
        // Get function name
        String functionName = ((Token) parseTree.children.get(1).payload).getText();

        // Get parameters
        List<String> args = new ArrayList<>();
        ParseTree typedArgsList = parseTree.children.get(2).children.get(1); // get node typedargslist

        for (ParseTree arg : typedArgsList.children) {
            if (arg.payload instanceof Token) {
                // when there is a single parameter, it will be directly under typedargslist
                Token argToken = (Token) arg.payload;
                if (argToken.getType() == Python3Lexer.NAME) args.add(argToken.getText());
            } else if (Objects.equals(String.valueOf(arg.payload), "tfpdef")) {
                // when there are two or more parameters, they will be under typedargslist/tfpdef
                assert arg.children.size() == 1;
                Object childPayload = arg.children.get(0).payload;
                assert childPayload instanceof Token;
                Token argToken = (Token) arg.children.get(0).payload;
                args.add(argToken.getText());
            } else {
                throw new AssertionError("multiple-argument func " +
                                                 "unexpected node: " + String.valueOf(arg.payload));
            }
        }

        PyNode funcNode = new PyFunctionNode(functionName,
                                             Arrays.copyOf(args.toArray(), args.toArray().length, String[].class));
        // TODO code

        System.out.println("Defining function '" + functionName + "' with args " + Arrays.toString(
                args.toArray()));

        return funcNode;
    }

    public PyNode parseStatement(ParseTree parseTree, int toIndex, PyNode currentPyNode) {
        if (toIndex < 0) return null;
        if (toIndex == 0) {
            return parseTermNode(parseTree.children.get(0));
        }

        if (parseTree.isChildToken(toIndex)) {
            Token firstToken = parseTree.astChildAsToken(toIndex);

            // Defining a function
            if (firstToken.getType() == Python3Lexer.DEF) {
                PyNode defPyNode = parseFuncDef(parseTree, currentPyNode);
                currentPyNode.addChild(defPyNode);
                return defPyNode;
            }
        }

        // +
        if (toIndex >= 2) { // At least three children
            if (parseTree.isChildToken(toIndex - 1)) {

                PyNode aritNode = null;
                switch (parseTree.astChildAsToken(toIndex - 1).getType()) {
                    case Python3Lexer.ADD:
                        aritNode = new PyAddNode();
                        break;
                    case Python3Lexer.MINUS:
                        aritNode = new PySubtractNode();
                        break;
                    default:
                        throw new NotImplementedException();
                }

                PyNode right = parseTermNode(parseTree.children.get(toIndex));
                PyNode left = parseStatement(parseTree, toIndex - 2, aritNode);

                aritNode.addChild(left);
                aritNode.addChild(right);
                return aritNode;

            }
        }
        throw new NotImplementedException();
    }

    final static int NODE_TYPE_TERM = 1;

    public int getNontokenType() {
        switch (String.valueOf(this.payload)) {
            case "term":
                return NODE_TYPE_TERM;
            default:
                throw new NotImplementedException();
        }
    }

    public PyNode parseTermNode(ParseTree parseTree) {
        if (parseTree.children.size() == 1 &&
                parseTree.isChildToken(0) &&
                parseTree.astChildAsToken(0).getType() == Python3Lexer.DECIMAL_INTEGER) {
            return new PyNumberNode(Long.parseLong(parseTree.astChildAsToken(0).getText()));
        }
        throw new NotImplementedException();
    }

    /**
     * Check if current ParseTree node is an instance of Token.
     */
    private boolean isToken() {
        return this.payload instanceof Token;
    }

    /**
     * Check if a child at a given position is an instance of Token.
     *
     * @param index Index of the child in ast.
     * @throws IndexOutOfBoundsException When child with such index does not exist.
     */
    private boolean isChildToken(int index) {
        return this.children.get(index).payload instanceof Token;
    }

    /**
     * Get a node's child and cast as Token.
     */
    private Token astChildAsToken(int index) {
        return (Token) this.children.get(index).payload;
    }

    public void traverse(ParseTree parseTree, PyNode currentPyNode) {
        // If there are only two children and the second one is newline, directly traverse the first child, discard newline
        if (parseTree.children.size() == 2 &&
                parseTree.isChildToken(1) &&
                parseTree.astChildAsToken(1).getType() == Python3Lexer.NEWLINE) {
            traverse(parseTree.children.get(0), currentPyNode);
            return;
        }

        if (!(parseTree.payload instanceof Token)) {
            switch (String.valueOf(parseTree.payload)) {
                case "file_input":
                    for (ParseTree child : parseTree.children) {
                        traverse(child, currentPyNode);
                    }
                    break;

                case "small_stmt":
                case "stmt":
                    PyNode node = parseStatement(parseTree, parseTree.children.size() - 1, currentPyNode);
                    currentPyNode.addChild(node);
                    break;

                default:
                    throw new NotImplementedException();
            }

        } else {
            System.out.println("here");
            Token token = (Token) parseTree.payload;
            int tokenCode = token.getType();

            String tokenEscaped = token.getText().replace("\n", "\\n").replace("\r", "\\r");
            switch (tokenCode) {
                case Python3Lexer.ADD:
                    PyNode newPyNode = new PyAddNode();
                    currentPyNode.addChild(newPyNode);
                    currentPyNode = newPyNode;
                    break;

                case Python3Lexer.DECIMAL_INTEGER:
                    currentPyNode.addChild(new PyNumberNode(token.getText()));
                    break;
            }


        }
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        ParseTree parseTree = this;
        List<ParseTree> firstStack = new ArrayList<>();
        firstStack.add(parseTree);

        List<List<ParseTree>> childListStack = new ArrayList<>();
        childListStack.add(firstStack);

        while (!childListStack.isEmpty()) {

            List<ParseTree> childStack = childListStack.get(childListStack.size() - 1);

            if (childStack.isEmpty()) {
                childListStack.remove(childListStack.size() - 1);
            } else {
                parseTree = childStack.remove(0);
                String caption;

                if (parseTree.payload instanceof Token) {
                    Token token = (Token) parseTree.payload;
                    String tokenEscaped = token.getText().replace("\n", "\\n").replace("\r", "\\r");
                    caption = String.format("TOKEN[type: %s, text: %s]",
                                            token.getType(), tokenEscaped);
//                    System.out.println("got token: " + tokenEscaped + " (" + token.getType() + ")");
                } else {
                    caption = String.valueOf(parseTree.payload);
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

                if (parseTree.children.size() > 0) {
                    List<ParseTree> children = new ArrayList<>();
                    for (int i = 0; i < parseTree.children.size(); i++) {
                        children.add(parseTree.children.get(i));
                    }
                    childListStack.add(children);
                }
            }
        }

        return builder.toString();
    }
}