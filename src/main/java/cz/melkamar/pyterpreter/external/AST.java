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

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
import jdk.internal.org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

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
 * In other word: all inner nodes that have a single child are removed from the AST.
 */
public class AST {

    /**
     * The payload will either be the name of the parser rule, or the token
     * of a leaf in the tree.
     */
    private final Object payload;

    /**
     * All child nodes of this AST.
     */
    private final List<AST> children;

    public AST(ParseTree tree) {
        this(null, tree);
    }

    private AST(AST ast, ParseTree tree) {
        this(ast, tree, new ArrayList<AST>());
    }

    private AST(AST parent, ParseTree tree, List<AST> children) {

        this.payload = getPayload(tree);
        this.children = children;

        if (parent == null) {
            // We're at the root of the AST, traverse down the parse tree to fill
            // this AST with nodes.
            walk(tree, this);
        } else {
            parent.children.add(this);
        }
    }

    public Object getPayload() {
        return payload;
    }

    public List<AST> getChildren() {
        return new ArrayList<>(children);
    }

    // Determines the payload of this AST: a string in case it's an inner node (which
    // is the name of the parser rule), or a Token in case it is a leaf node.
    private Object getPayload(ParseTree tree) {
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

    // Fills this AST based on the parse tree.
    private static void walk(ParseTree tree, AST ast) {

        if (tree.getChildCount() == 0) {
            // We've reached a leaf. We must create a new instance of an AST because
            // the constructor will make sure this new instance is added to its parent's
            // child nodes.
            new AST(ast, tree);
        } else if (tree.getChildCount() == 1) {
            // We've reached an inner node with a single child: we don't include this in
            // our AST.
            walk(tree.getChild(0), ast);
        } else if (tree.getChildCount() > 1) {

            for (int i = 0; i < tree.getChildCount(); i++) {

                AST temp = new AST(ast, tree.getChild(i));

                if (!(temp.payload instanceof Token)) {
                    // Only traverse down if the payload is not a Token.
                    walk(tree.getChild(i), temp);
                }
            }
        }
    }

    class Node {
        String text;
        int type;
        Node parent;
        List<Node> children;

        public Node(String text, int type, Node parent) {
            this.text = text;
            this.type = type;
            this.parent = parent;
            children = new ArrayList<>();
        }

        public void addChild(Node child) {
            children.add(child);
        }
    }

    public void doTraversal() {
        Node rootNode = new Node("ROOT", -1, null);
        traverse(this, rootNode);
        System.out.println(rootNode);
    }

    private Node parseFuncDef(AST ast, Node currentNode){
        // Get function name
        String functionName = ((Token) ast.children.get(1).payload).getText();

        // Get parameters
        List<String> args = new ArrayList<>();
        AST typedArgsList = ast.children.get(2).children.get(1); // get node typedargslist

        for (AST arg : typedArgsList.children) {
            if (arg.payload instanceof Token) {
                // when there is a single parameter, it will be directly under typedargslist
                Token argToken = (Token) arg.payload;
                if (argToken.getType() == Python3Lexer.NAME) args.add(argToken.getText());
            } else if (Objects.equals(String.valueOf(arg.payload), "tfpdef")) {
                // when there are two or more parameters, they will be under typedargslist/tfpdef
                assert arg.children.size()==1;
                Object childPayload = arg.children.get(0).payload;
                assert childPayload instanceof Token;
                Token argToken = (Token) arg.children.get(0).payload;
                args.add(argToken.getText());
            } else {
                throw new AssertionError("multiple-argument func " +
                                                 "unexpected node: " + String.valueOf(arg.payload));
            }
        }

        // Get code
        // TODO

        Node defNode = new Node("DEF", Python3Lexer.DEF, currentNode);
        Node nameNode = new Node(functionName, Python3Lexer.NAME, defNode);
        Node argListNode = new Node("paramlist "+Arrays.toString(args.toArray()), -1, defNode);
        defNode.addChild(nameNode);
        defNode.addChild(argListNode);

        System.out.println("Defining function '" + functionName + "' with args " + Arrays.toString(
                args.toArray()));

        return defNode;
    }

    public void traverse(AST ast, Node currentNode) {
        if (!(ast.payload instanceof Token)) {
            switch (String.valueOf(ast.payload)) {
                case "small_stmt":
                case "stmt":
                    if (ast.children.get(0).payload instanceof Token) {
                        Token firstToken = (Token) ast.children.get(0).payload;

                        // Defining a function
                        if (firstToken.getType() == Python3Lexer.DEF) {
                            Node defNode = parseFuncDef(ast, currentNode);
                            currentNode.addChild(defNode);
                        }
                    }

            }

        } else {
            Token token = (Token) ast.payload;
            int tokenCode = token.getType();

            String tokenEscaped = token.getText().replace("\n", "\\n").replace("\r", "\\r");
//            System.out.println("    --  got token: " + tokenEscaped + " (" + token.getType() + ")");

            switch (tokenCode) {
                case Python3Lexer.ADD:
                    Node newNode = new Node("+", tokenCode, currentNode);
                    currentNode.addChild(newNode);
                    currentNode = newNode;
                    break;

                case Python3Lexer.DECIMAL_INTEGER:
                    currentNode.addChild(new Node(tokenEscaped, Python3Lexer.DECIMAL_INTEGER, currentNode));
                    break;
            }


        }

        for (AST child : ast.children) {
            traverse(child, currentNode);
        }
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        AST ast = this;
        List<AST> firstStack = new ArrayList<>();
        firstStack.add(ast);

        List<List<AST>> childListStack = new ArrayList<>();
        childListStack.add(firstStack);

        while (!childListStack.isEmpty()) {

            List<AST> childStack = childListStack.get(childListStack.size() - 1);

            if (childStack.isEmpty()) {
                childListStack.remove(childListStack.size() - 1);
            } else {
                ast = childStack.remove(0);
                String caption;

                if (ast.payload instanceof Token) {
                    Token token = (Token) ast.payload;
                    String tokenEscaped = token.getText().replace("\n", "\\n").replace("\r", "\\r");
                    caption = String.format("TOKEN[type: %s, text: %s]",
                                            token.getType(), tokenEscaped);
                    System.out.println("got token: " + tokenEscaped + " (" + token.getType() + ")");
                } else {
                    caption = String.valueOf(ast.payload);
                    System.out.println("got non-token: " + caption);
                }

                String indent = "";

                for (int i = 0; i < childListStack.size() - 1; i++) {
                    indent += (childListStack.get(i).size() > 0) ? "|  " : "   ";
                }

                builder.append(indent)
                        .append(childStack.isEmpty() ? "'- " : "|- ")
                        .append(caption)
                        .append("\n");

                if (ast.children.size() > 0) {
                    List<AST> children = new ArrayList<>();
                    for (int i = 0; i < ast.children.size(); i++) {
                        children.add(ast.children.get(i));
                    }
                    childListStack.add(children);
                }
            }
        }

        return builder.toString();
    }

//    public static void main(String[] args) {

    // Generate the parser and lexer classes below using the grammar available here:
    // https://github.com/bkiers/python3-parser
//        Python3Lexer lexer = new Python3Lexer(new ANTLRInputStream("f(arg1='1')\n"));
//        Python3Parser parser = new Python3Parser(new CommonTokenStream(lexer));

//        ParseTree tree = parser.file_input();
//        AST ast = new AST(tree);
//
//        System.out.println(ast);

    // Output:
    //
    //    '- file_input
    //       |- stmt
    //       |  |- small_stmt
    //       |  |  |- atom
    //       |  |  |  '- TOKEN[type: 35, text: f]
    //       |  |  '- trailer
    //       |  |     |- TOKEN[type: 47, text: (]
    //       |  |     |- arglist
    //       |  |     |  |- test
    //       |  |     |  |  '- TOKEN[type: 35, text: arg1]
    //       |  |     |  |- TOKEN[type: 53, text: =]
    //       |  |     |  '- test
    //       |  |     |     '- TOKEN[type: 36, text: '1']
    //       |  |     '- TOKEN[type: 48, text: )]
    //       |  '- TOKEN[type: 34, text: \n]
    //       '- TOKEN[type: -1, text: <EOF>]
//    }
}