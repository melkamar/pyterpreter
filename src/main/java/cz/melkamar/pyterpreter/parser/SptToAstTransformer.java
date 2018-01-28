package cz.melkamar.pyterpreter.parser;

import com.oracle.truffle.api.frame.FrameSlot;
import cz.melkamar.pyterpreter.antlr.Python3Lexer;
import cz.melkamar.pyterpreter.antlr.Python3Parser;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.nodes.*;
import cz.melkamar.pyterpreter.nodes.expr.arithmetic.PyAddNodeGen;
import cz.melkamar.pyterpreter.nodes.expr.arithmetic.PyDivideNodeGen;
import cz.melkamar.pyterpreter.nodes.expr.arithmetic.PyMultiplyNodeGen;
import cz.melkamar.pyterpreter.nodes.expr.arithmetic.PySubtractNodeGen;
import cz.melkamar.pyterpreter.nodes.expr.literal.PyBigNumLitNode;
import cz.melkamar.pyterpreter.nodes.expr.literal.PyBooleanLitNode;
import cz.melkamar.pyterpreter.nodes.expr.literal.PyLongLitNode;
import cz.melkamar.pyterpreter.nodes.expr.literal.PyStringLitNode;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class SptToAstTransformer {
    final static String NODE_STR_TERM = "term";
    final static String NODE_STR_FACTOR = "factor";
    final static String NODE_STR_TESTLIST_EXPR_STAR = "testlist_star_expr";
    final static String NODE_STR_STAR_EXPR = "star_expr";
    final static String NODE_STR_STMT = "stmt";
    final static String NODE_STR_SMALL_STMT = "small_stmt";
    final static String NODE_STR_FILE_INPUT = "file_input";
    final static String NODE_STR_ATOM = "atom";
    final static String NODE_STR_TRAILER = "trailer";
    final static String NODE_STR_ARGLIST = "arglist";
    final static String NODE_STR_ARGUMENT = "argument";
    final static String NODE_STR_TEST = "test";
    final static String NODE_STR_COMP_OP = "comp_op";

    /**
     * Parse function call argument list, e.g.
     * <p>
     * |- arglist
     * |  |- argument
     * |  |  '- TOKEN[type: 38, text: 1]
     * |  |- TOKEN[type: 49, text: ,]
     * |  '- argument
     * |     |- term
     * |     |  '- TOKEN[type: 38, text: 2]
     * |     |- TOKEN[type: 61, text: +]
     * |     '- term
     * |        '- TOKEN[type: 38, text: 3]
     * <p>
     * Check if the first child of arglist is "argument" - if the first node is not "argument",
     * then there is only one argument and the node was deleted (because all nodes with a single
     * child were squished). It is like this:
     * <p>
     * |- arglist
     * |  |- term
     * |  |  '- TOKEN[type: 38, text: 1234]
     * |  |- TOKEN[type: 61, text: +]
     * |  '- term
     * |     '- TOKEN[type: 38, text: 1]
     * '- TOKEN[type: 48, text: )]
     * <p>
     * In this case parse arglist as if it was an "argument" node.
     */
//    public static List<PyNode> parseArgList(SimpleParseTree simpleParseTree) {
//        assert simpleParseTree.getPayloadAsString().equals(NODE_STR_ARGLIST);
//        List<PyNode> result = new ArrayList<>();
//
//        // For explanation of this special case see javadoc.
//        if (!simpleParseTree.getChildPayload(0).equals(NODE_STR_ARGUMENT)) {
//            PyNode argNode = parseExpression(simpleParseTree);
//            result.add(argNode);
//            return result;
//        }
//
//
//        for (SimpleParseTree child : simpleParseTree.getChildren()) {
//            if (child.isToken()) { // If a child is directly a token, it should be a comma separating arguments in list
//                if (child.asToken().getType() == Python3Parser.COMMA) continue; // Skip commas in arglist
//                throw new NotImplementedException("Parsing arglist, got token type " + child.asToken().getType());
//            }
//
//            PyNode argNode = parseExpression(child);
//            result.add(argNode);
//        }
//
//        return result;
//    }

    /**
     * Parse if-elif-else statement, e.g.
     * <p>
     * stmt
     * |- TOKEN[type: 10, text: if]
     * |- test
     * |  |- star_expr
     * |  |  '- TOKEN[type: 38, text: 1]
     * |  |- comp_op
     * |  |  '- TOKEN[type: 71, text: ==]
     * |  '- star_expr
     * |     '- TOKEN[type: 38, text: 2]
     * |- TOKEN[type: 50, text: :]
     * |- suite
     * |  |- TOKEN[type: 34, text:  ]
     * |  |- TOKEN[type: 93, text:     ]
     * |  |- stmt
     * |  |  |- small_stmt
     * |  |  |  |- atom
     * |  |  |  |  '- TOKEN[type: 35, text: print]
     * |  |  |  '- trailer
     * |  |  |     |- TOKEN[type: 47, text: (]
     * |  |  |     |- arglist
     * |  |  |     |  '- TOKEN[type: 38, text: 1]
     * |  |  |     '- TOKEN[type: 48, text: )]
     * |  |  '- TOKEN[type: 34, text: \n]
     * |  '- TOKEN[type: 94, text: \n]
     *
     * @param simpleParseTree
     * @return
     */
//    public static PyNode parseIfStmt(SimpleParseTree simpleParseTree) {
//        assert simpleParseTree.isChildToken(0) &&
//                simpleParseTree.childAsToken(0).getType() == Python3Parser.IF;
//
//        PyNode testNode = parseIfTestCondition(simpleParseTree.getChild(1));
//        PyNode doIfTrueNode = parseCodeBlock(simpleParseTree.getChild(3));
//        PyNode doIfFalseNode = null;
//
//        if (simpleParseTree.getChildCount() > 4) {
//            if (simpleParseTree.childAsToken(4).getType() == Python3Parser.ELSE) {
//                doIfFalseNode = parseCodeBlock(simpleParseTree.getChild(6));
//            } else {
//                throw new NotImplementedException("elif not implemented");
//            }
//        }
//
//        IfNode ifNode = new IfNode();
//        ifNode.addChild(testNode);
//        ifNode.addChild(doIfTrueNode);
//        if (doIfFalseNode != null) ifNode.addChild(doIfFalseNode);
//        return ifNode;
//    }

    /**
     * Parse test condition, e.g. x==1 and y==2 or not z==3
     *
     * @param simpleParseTree
     * @return
     */
//    public static PyNode parseIfTestCondition(SimpleParseTree simpleParseTree) {
//        // Case of three children and first and last are parentheses - just return parsed middle child
//        if (simpleParseTree.getChildCount() == 3 &&
//                simpleParseTree.isChildToken(0) &&
//                simpleParseTree.isChildToken(2) &&
//                simpleParseTree.childAsToken(0).getType() == Python3Parser.OPEN_PAREN &&
//                simpleParseTree.childAsToken(2).getType() == Python3Parser.CLOSE_PAREN
//                ) return parseIfTestCondition(simpleParseTree.getChild(1));
//
//        // Case of "not <expr>"
//        if (simpleParseTree.getChildCount() == 2 && simpleParseTree.isChildToken(0)) {
//            if (simpleParseTree.childAsToken(0).getType() == Python3Parser.NOT) {
//                NotNode notNode = new NotNode();
//                notNode.addChild(parseIfTestCondition(simpleParseTree.getChild(1)));
//                return notNode;
//            }
//            throw new NotImplementedException("Test node has two children, but first is not 'not'");
//        }
//
//        // Case of some combination of and/or
//        if (simpleParseTree.getChildCount() > 1 && simpleParseTree.isChildToken(1)) {
//            PyNode opNode;
//            switch (simpleParseTree.childAsToken(1).getType()) {
//                case Python3Parser.OR:
//                    opNode = new OrNode();
//                    break;
//                case Python3Parser.AND:
//                    opNode = new AndNode();
//                    break;
//                default:
//                    return parseExpression(simpleParseTree);
//            }
//            for (int i = 0; i < simpleParseTree.getChildCount(); i += 2) {
//                opNode.addChild(parseIfTestCondition(simpleParseTree.getChild(i)));
//            }
//            return opNode;
//        }
//
//        // If neither "not" nor "and/or", just parse the current node as an expression
//        return parseExpression(simpleParseTree);
//    }

//    public static PyNode parseFuncDef(SimpleParseTree simpleParseTree) {
//        // Get function name
//        String functionName = ((Token) simpleParseTree.getChildPayload(1)).getText();
//
//        // Get parameters
//        List<String> args = new ArrayList<>();
//        SimpleParseTree typedArgsList = simpleParseTree.getChild(2).getChild(1); // get node typedargslist
//
//        for (SimpleParseTree arg : typedArgsList.getChildren()) {
//            if (arg.getPayload() instanceof Token) {
//                // when there is a single parameter, it will be directly under typedargslist
//                Token argToken = (Token) arg.getPayload();
//                if (argToken.getType() == Python3Lexer.NAME) args.add(argToken.getText());
//            } else if (Objects.equals(String.valueOf(arg.getPayload()), "tfpdef")) {
//                // when there are two or more parameters, they will be under typedargslist/tfpdef
//                assert arg.getChildCount() == 1;
//                Object childPayload = arg.getChildPayload(0);
//                assert childPayload instanceof Token;
//                Token argToken = (Token) childPayload;
//                args.add(argToken.getText());
//            } else {
//                throw new AssertionError("multiple-argument func " +
//                                                 "unexpected node: " + String.valueOf(arg.getPayload()));
//            }
//        }
//
//        SimpleParseTree suiteNode = simpleParseTree.getChild(4);
//        CodeBlockNode funcCode = parseCodeBlock(suiteNode);
//
//        PyDefFuncNode funcNode = new PyDefFuncNode(functionName,
//                                                   Arrays.copyOf(args.toArray(),
//                                                                 args.toArray().length,
//                                                                 String[].class));
//
//        for (PyNode funcNodeChild : funcCode.getChildNodes())
//            funcNode.addChild(funcNodeChild);
//
//        return funcNode;
//    }

    /**
     * Clean case when there are two child nodes, but the second one is just a newline.
     * This method either returns the SPT passed as parameter if there is no trailing newline, or returns the
     * non-newline node if it has a newline sibling.
     *
     * @param simpleParseTree
     * @return
     */
    private static SimpleParseTree removeTrailingNewline(SimpleParseTree simpleParseTree) {
        if (simpleParseTree.getChildCount() == 2 &&
                simpleParseTree.isChildToken(1) &&
                simpleParseTree.childAsToken(1).getType() == Python3Lexer.NEWLINE) {
            return simpleParseTree.getChild(0);
        }

        return simpleParseTree;
    }

    /**
     * Convert a statement node (stmt) and its body into a AST subtree.
     *
     * @param simpleParseTree ParseTree node to convert.
     * @return Newly created root of an AST subtree.
     */
    public static PyStatementNode parseStatement(SimpleParseTree simpleParseTree) {
        simpleParseTree = removeTrailingNewline(simpleParseTree);
        assert simpleParseTree.getPayloadAsString().equals(NODE_STR_STMT) ||
                simpleParseTree.getPayloadAsString().equals(NODE_STR_SMALL_STMT);

        if (simpleParseTree.getChildCount() == 0) {
//            return parseTermNode(simpleParseTree.body.get(0));
            // TODO
            throw new NotImplementedException();
        }

        if (simpleParseTree.isChildToken(0)) {
            Token firstToken = simpleParseTree.childAsToken(0);

            // Defining a function?
            if (firstToken.getType() == Python3Lexer.DEF) {
                // TODO
                throw new NotImplementedException();
//                return parseFuncDef(simpleParseTree);
            }

            if (firstToken.getType() == Python3Parser.IF) {
                // TODO
                throw new NotImplementedException();
//                return parseIfStmt(simpleParseTree);
            }
        }

        if (simpleParseTree.getChildCount() == 1 && simpleParseTree.isChildToken(0) &&
                simpleParseTree.childAsToken(0).getType() == Python3Parser.RETURN) {
            // TODO
            throw new NotImplementedException();
//            return new ReturnNode();
        }

        // return xxx statement?
        if (simpleParseTree.getChildCount() == 2) {
            if (simpleParseTree.isChildToken(0) &&
                    simpleParseTree.childAsToken(0).getType() == Python3Parser.RETURN) {
                // TODO
                throw new NotImplementedException();
//                ReturnNode returnNode = new ReturnNode();
//                PyNode returnBody = parseExpression(simpleParseTree.getChild(1));
//                returnNode.addChild(returnBody);
//                return returnNode;
            }
        }

        if (simpleParseTree.getChildCount() >= 3) {
            if (simpleParseTree.isChildToken(1)) {
                Token secondToken = simpleParseTree.childAsToken(1);

                // Is this assignment? e.g.   x = 5 + 4
                if (secondToken.getType() == Python3Lexer.ASSIGN) {
                    PyExpressionNode varNameNode = parseExpression(simpleParseTree, 0, 0);

                    // Parsing a NAME will yield a ReadVarNode, but in this special case (lhs in assignment) we only
                    // care about the variable name (and do not want to read anything)
                    String varName = ((PyReadVarNode) varNameNode).getVarName();
                    PyExpressionNode assignValueNode = parseExpression(simpleParseTree,
                                                                            2,
                                                                            simpleParseTree.getChildCount() - 1);
                    FrameSlot slot = SimpleParseTree.frameDescriptor.findOrAddFrameSlot(varName);
                    PyAssignNode assignNode = PyAssignNodeGen.create(assignValueNode, slot);

                    return assignNode;
                }
            }
        }

        return parseExpression(simpleParseTree, 0, simpleParseTree.getChildCount() - 1);
    }

    public static PyExpressionNode parseToken(SimpleParseTree simpleParseTree) {
        assert simpleParseTree.isToken();

        if (simpleParseTree.isToken()) {
            if (simpleParseTree.asToken().getType() == Python3Lexer.DECIMAL_INTEGER) {
                try {
                    return new PyLongLitNode(Long.parseLong(simpleParseTree.asToken().getText()));
                } catch (NumberFormatException e){
                    return new PyBigNumLitNode(simpleParseTree.asToken().getText());
                }
            }

            if (simpleParseTree.asToken().getType() == Python3Lexer.NAME) {
                FrameSlot slot = SimpleParseTree.frameDescriptor.findOrAddFrameSlot(simpleParseTree.asToken().getText());
                return PyReadVarNodeGen.create(slot);
            }

            if (simpleParseTree.asToken().getType() == Python3Parser.STRING_LITERAL) {
                return new PyStringLitNode(simpleParseTree.asToken().getText());
            }

            if (simpleParseTree.asToken().getType() == Python3Parser.TRUE) {
                return new PyBooleanLitNode(true);
            }

            if (simpleParseTree.asToken().getType() == Python3Parser.FALSE) {
                return new PyBooleanLitNode(false);
            }

            if (simpleParseTree.asToken().getType() == Python3Parser.FLOAT_NUMBER) {
                // TODO
                throw new NotImplementedException();
//                return new FloatingNumberNode(simpleParseTree.asToken().getText());
            }
        }

        throw new NotImplementedException();
    }

    /**
     * Convenience method for parseExpression(SPT, from, to).
     */
    public static PyExpressionNode parseExpression(SimpleParseTree simpleParseTree) {
        return parseExpression(simpleParseTree, 0, simpleParseTree.getChildCount() - 1);
    }

    /**
     * Convert an expression into AST subtree.
     * Only work with a subset of nodes, specified by fromIndex and toIndex (both inclusive).
     * <p>
     * E.g. Expression in the form of 1+2+3+4+5 will take several runs to be converted to binary AST:
     * <p>
     * ...(+)
     * .1.....parse(2+3+4+5)
     * <p>
     * ....v
     * ....v
     * <p>
     * ......(+)
     * ....1.....(+)
     * .........2...parse(3+4+5)
     * In each step the expression is the same - only indices differ.
     *
     * @param simpleParseTree Expression node of the parsetree.
     * @param fromIndex       Index of child from which to start (inclusive).
     * @param toIndex         Index of child at which to end (inclusive)
     * @return Root of the new AST subtree.
     */
    public static PyExpressionNode parseExpression(SimpleParseTree simpleParseTree, int fromIndex, int toIndex) {
        if (toIndex < fromIndex) {
            // This node does not have body - either a token or WTF
            if (simpleParseTree.isToken()) {
                return parseToken(simpleParseTree);
            }
            throw new NotImplementedException("No body for node, but node not Token.");
        }

        if (toIndex == fromIndex) {
            // This node has a single child - parse it as expression
            return parseExpression(simpleParseTree.getChild(toIndex),
                                   0,
                                   simpleParseTree.getChild(toIndex).getChildCount() - 1);
        }

        // Two body - function call?
        if (toIndex - fromIndex == 1) {
            // Check if first child is atom and its child a token
            if (simpleParseTree.getChild(0).getPayloadAsString().equals(NODE_STR_ATOM) &&
                    simpleParseTree.getChild(0).getChildCount() == 1 &&
                    simpleParseTree.getChild(0).isChildToken(0)) {
                String funcName = simpleParseTree.getChild(0).childAsToken(0).getText();

                assert simpleParseTree.getChild(1).getPayloadAsString().equals(NODE_STR_TRAILER);

                SimpleParseTree arglistNode = simpleParseTree.getChild(1).getChild(1);

                // TODO
                throw new NotImplementedException();
//                FunctionCallNode callNode = new FunctionCallNode(funcName);
//                if (arglistNode.getPayloadAsString().equals(NODE_STR_ARGLIST)) {
//                    callNode.addChildren(parseArgList(arglistNode));
//                }
//                return callNode;
            }
        }

        // Binary operation?
        if (toIndex - fromIndex >= 2) { // At least three body
            // Check if first and last token is parenthesis - if so, skip them
            if (simpleParseTree.isChildToken(0) &&
                    simpleParseTree.childAsToken(0).getType() == Python3Lexer.OPEN_PAREN &&
                    simpleParseTree.isChildToken(simpleParseTree.getChildCount() - 1) &&
                    simpleParseTree.childAsToken(simpleParseTree.getChildCount() - 1)
                            .getType() == Python3Lexer.CLOSE_PAREN) {
                return parseExpression(simpleParseTree, 1, simpleParseTree.getChildCount() - 2);
            }

            if (simpleParseTree.isChildToken(toIndex - 1)) {

                PyExpressionNode right = parseExpression(simpleParseTree, toIndex, toIndex);
                PyExpressionNode left = parseExpression(simpleParseTree, fromIndex, toIndex - 2);

                switch (simpleParseTree.childAsToken(toIndex - 1).getType()) {
                    case Python3Lexer.ADD:
                        return PyAddNodeGen.create(left, right);
                    case Python3Lexer.MINUS:
                        return PySubtractNodeGen.create(left, right);
                    case Python3Lexer.STAR:
                        return PyMultiplyNodeGen.create(left, right);
                    case Python3Lexer.DIV:
                        return PyDivideNodeGen.create(left, right);
                    case Python3Lexer.EQUALS:
                        // TODO
                        throw new NotImplementedException();
//                        binaryNode = new EqualsNode();
//                        break;
                    default:
                        throw new NotImplementedException();
                }
            }

            /*
             * Test expression?
             * |- test
             * |  |- star_expr
             * |  |  '- TOKEN[type: 35, text: x]
             * |  |- comp_op
             * |  |  '- TOKEN[type: 71, text: ==]
             * |  '- star_expr
             * |     '- TOKEN[type: 38, text: 0]
             */
            if (simpleParseTree.getChildPayload(toIndex - 1).equals(NODE_STR_COMP_OP)) {
                // TODO
                throw new NotImplementedException();
//                PyNode compNode;
//                switch (simpleParseTree.getChild(toIndex - 1).childAsToken(0).getType()) {
//                    case Python3Parser.EQUALS:
//                        compNode = new EqualsNode();
//                        break;
//                    case Python3Parser.NOT_EQ_1:
//                    case Python3Parser.NOT_EQ_2:
//                        compNode = new NotEqualsNode();
//                        break;
//
//                    default:
//                        throw new NotImplementedException("comp_op not implemented: " + simpleParseTree.getChild(toIndex - 1)
//                                .childAsToken(0)
//                                .getType());
//                }
//
//                PyNode right = parseExpression(simpleParseTree, toIndex, toIndex);
//                PyNode left = parseExpression(simpleParseTree, fromIndex, toIndex - 2);
//
//                compNode.addChild(left);
//                compNode.addChild(right);
//                return compNode;
            }
        }
        throw new NotImplementedException("Got '" + simpleParseTree.getPayloadAsString() + "' from " + fromIndex + " to " + toIndex);
    }

    public static PySuiteNode parseCodeBlock(SimpleParseTree suiteNode) {
        assert suiteNode.getPayloadAsString().equals("suite");

        List<PyStatementNode> statementNodes = new ArrayList<>();
        for (SimpleParseTree child : suiteNode.getChildren()) {
            if (child.isToken()) {
                int tokenType = child.asToken().getType();
                if (tokenType == Python3Lexer.NEWLINE ||
                        tokenType == Python3Parser.INDENT ||
                        tokenType == Python3Parser.DEDENT)
                    continue;
            }

            statementNodes.add(parseStatement(child));
        }

        return new PySuiteNode(statementNodes.toArray(new PyStatementNode[statementNodes.size()]), false);
    }

    public static PySuiteNode parseFileInputBlock(SimpleParseTree fileInputNode) {
        assert fileInputNode.getPayloadAsString().equals(NODE_STR_FILE_INPUT);

        List<PyStatementNode> statementNodes = new ArrayList<>();
        for (SimpleParseTree child : fileInputNode.getChildren()) {
            if (child.isToken()) {
                int tokenType = child.asToken().getType();
                if (tokenType == Python3Lexer.NEWLINE || tokenType == Python3Parser.EOF)
                    continue;
            }

            PyStatementNode statementNode = parseStatement(child);
            statementNodes.add(statementNode);
        }

        // TODO do I really want to always return last result? Should I detect if I am in REPL mode?
        return new PySuiteNode(statementNodes.toArray(new PyStatementNode[statementNodes.size()]), true);
    }
}
