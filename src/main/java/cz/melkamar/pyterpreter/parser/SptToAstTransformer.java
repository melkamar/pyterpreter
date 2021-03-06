package cz.melkamar.pyterpreter.parser;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import cz.melkamar.pyterpreter.antlr.Python3Lexer;
import cz.melkamar.pyterpreter.antlr.Python3Parser;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.functions.PyUserFunction;
import cz.melkamar.pyterpreter.nodes.*;
import cz.melkamar.pyterpreter.nodes.control.*;
import cz.melkamar.pyterpreter.nodes.expr.PyNotNodeGen;
import cz.melkamar.pyterpreter.nodes.expr.arithmetic.*;
import cz.melkamar.pyterpreter.nodes.expr.compare.*;
import cz.melkamar.pyterpreter.nodes.expr.literal.*;
import cz.melkamar.pyterpreter.nodes.function.*;
import org.antlr.v4.runtime.Token;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    final static String NODE_STR_AUGASSIGN = "augassign";

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
    public static List<PyExpressionNode> parseArgList(SimpleParseTree simpleParseTree,
                                                      FrameDescriptor frameDescriptor) {
        assert simpleParseTree.getPayloadAsString().equals(NODE_STR_ARGLIST);
        List<PyExpressionNode> result = new ArrayList<>();

        // For explanation of this special case see javadoc.
        if (!simpleParseTree.getChildPayload(0).equals(NODE_STR_ARGUMENT)) {
            PyExpressionNode argNode = parseExpression(simpleParseTree, frameDescriptor);
            result.add(argNode);
            return result;
        }


        for (SimpleParseTree child : simpleParseTree.getChildren()) {
            if (child.isToken()) { // If a child is directly a token, it should be a comma separating arguments in list
                if (child.asToken().getType() == Python3Parser.COMMA) continue; // Skip commas in arglist
                throw new NotImplementedException("Parsing arglist, got token type " + child.asToken().getType());
            }

            PyExpressionNode argNode = parseExpression(child, frameDescriptor);
            result.add(argNode);
        }

        return result;
    }

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
    public static PyStatementNode parseIfStmt(SimpleParseTree simpleParseTree, FrameDescriptor frameDescriptor) {
        assert simpleParseTree.isChildToken(0) &&
                simpleParseTree.childAsToken(0).getType() == Python3Parser.IF;

        PyExpressionNode testNode = parseIfTestCondition(simpleParseTree.getChild(1), frameDescriptor);
        PyStatementNode doIfTrueNode = parseCodeBlock(simpleParseTree.getChild(3), frameDescriptor);
        PyStatementNode doIfFalseNode = null;

        if (simpleParseTree.getChildCount() > 4) {
            if (simpleParseTree.childAsToken(4).getType() == Python3Parser.ELSE) {
                doIfFalseNode = parseCodeBlock(simpleParseTree.getChild(6), frameDescriptor);
            } else {
                throw new NotImplementedException("elif not implemented");
            }
        }

        PyIfNode ifNode = new PyIfNode(testNode, doIfTrueNode, doIfFalseNode);
        return ifNode;
    }

    public static PyStatementNode parseWhileStmt(SimpleParseTree simpleParseTree, FrameDescriptor frameDescriptor) {
        assert simpleParseTree.isChildToken(0) &&
                simpleParseTree.childAsToken(0).getType() == Python3Parser.WHILE;

        PyExpressionNode conditionNode = parseIfTestCondition(simpleParseTree.getChild(1), frameDescriptor);
        PyStatementNode bodyNode = parseCodeBlock(simpleParseTree.getChild(3), frameDescriptor);

        PyWhileNode ifNode = new PyWhileNode(conditionNode, bodyNode);
        return ifNode;
    }

    /**
     * Create a OR or AND node. In Java we need this ugliness because static methods cannot be abstract and overridden.
     * And I don't want to bother with making factory classes.
     *
     * @param nodeClass Class of the node to create, it must have a constructor with two {@link PyExpressionNode}
     *                  parameters.
     * @param left      Left child node.
     * @param right     Right child node.
     * @return Newly created node.
     */
    private static PyExpressionNode createLogicalNode(Class nodeClass, PyExpressionNode left, PyExpressionNode right) {
        try {
            return (PyExpressionNode) nodeClass.getDeclaredConstructor(PyExpressionNode.class, PyExpressionNode.class)
                    .newInstance(left, right);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new NotImplementedException("something has gone wrong with and|or");
        }
    }

    /**
     * Parse test condition, e.g. x==1 and y==2 or not z==3
     *
     * @param simpleParseTree
     * @return
     */
    public static PyExpressionNode parseIfTestCondition(SimpleParseTree simpleParseTree,
                                                        FrameDescriptor frameDescriptor) {
        // Case of three children and first and last are parentheses - just return parsed middle child
        if (simpleParseTree.getChildCount() == 3 &&
                simpleParseTree.isChildToken(0) &&
                simpleParseTree.isChildToken(2) &&
                simpleParseTree.childAsToken(0).getType() == Python3Parser.OPEN_PAREN &&
                simpleParseTree.childAsToken(2).getType() == Python3Parser.CLOSE_PAREN
                ) return parseIfTestCondition(simpleParseTree.getChild(1), frameDescriptor);

        // Case of "not <expr>"
        if (simpleParseTree.getChildCount() == 2 && simpleParseTree.isChildToken(0)) {
            if (simpleParseTree.childAsToken(0).getType() == Python3Parser.NOT) {
                PyExpressionNode expressionNode = parseIfTestCondition(simpleParseTree.getChild(1), frameDescriptor);
                return PyNotNodeGen.create(expressionNode);
            }
            throw new NotImplementedException("Test node has two children, but first is not 'not'");
        }

        // Case of some combination of and/or
        if (simpleParseTree.getChildCount() > 1 && simpleParseTree.isChildToken(1)) {

            // We can go through a flat list of nodes because when parsetree contains a parent with multiple children,
            // they are always joined with the same logical operator (different operator causes nesting)
            List<PyExpressionNode> condNodes = new ArrayList<>();
            for (int i = 0; i < simpleParseTree.getChildCount(); i += 2) {
                condNodes.add(parseIfTestCondition(simpleParseTree.getChild(i), frameDescriptor));
            }

            Class logicalNodeClass; // TODO or or and?
            switch (simpleParseTree.childAsToken(1).getType()) {
                case Python3Parser.OR:
                    logicalNodeClass = PyOrNode.class;
                    break;
                case Python3Parser.AND:
                    logicalNodeClass = PyAndNode.class;
                    break;
                default:
                    return parseExpression(simpleParseTree, frameDescriptor);
            }

            PyExpressionNode lastLogicalNode = createLogicalNode(logicalNodeClass,
                                                                 condNodes.get(condNodes.size() - 2),
                                                                 condNodes.get(condNodes.size() - 1));

            for (int i = condNodes.size() - 3; i >= 0; i--) {
                PyExpressionNode newLogicalNode = createLogicalNode(logicalNodeClass,
                                                                    condNodes.get(i),
                                                                    lastLogicalNode);
                lastLogicalNode = newLogicalNode;
            }

            return lastLogicalNode;
        }

        // If neither "not" nor "and/or", just parse the current node as an expression
        return parseExpression(simpleParseTree, frameDescriptor);
    }

    public static PyDefFuncNode parseFuncDef(SimpleParseTree simpleParseTree, FrameDescriptor frameDescriptor) {
        // Get function name
        String functionName = ((Token) simpleParseTree.getChildPayload(1)).getText();

        // Get parameters
        List<String> args = new ArrayList<>();
        SimpleParseTree typedArgsList = simpleParseTree.getChild(2).getChild(1); // get node typedargslist

        for (SimpleParseTree arg : typedArgsList.getChildren()) {
            if (arg.getPayload() instanceof Token) {
                // when there is a single parameter, it will be directly under typedargslist
                Token argToken = (Token) arg.getPayload();
                if (argToken.getType() == Python3Lexer.NAME) args.add(argToken.getText());
            } else if (Objects.equals(String.valueOf(arg.getPayload()), "tfpdef")) {
                // when there are two or more parameters, they will be under typedargslist/tfpdef
                assert arg.getChildCount() == 1;
                Object childPayload = arg.getChildPayload(0);
                assert childPayload instanceof Token;
                Token argToken = (Token) childPayload;
                args.add(argToken.getText());
            } else {
                throw new AssertionError("multiple-argument function " +
                                                 "unexpected node: " + String.valueOf(arg.getPayload()));
            }
        }

        FrameDescriptor newFrameDescriptor = new FrameDescriptor();
        List<PyStatementNode> initParamStatements = new ArrayList<>(args.size());
        for (int i = 0; i < args.size(); i++) {
            String argName = args.get(i);
            PyExpressionNode assignValueNode = new PyReadArgNode(i);
            FrameSlot slot = newFrameDescriptor.findOrAddFrameSlot(argName);
            PyAssignNode assignNode = PyAssignNodeGen.create(assignValueNode, slot);
            initParamStatements.add(assignNode);
        }

        SimpleParseTree suiteNode = simpleParseTree.getChild(4);

        // Create hierarchy RootNode -> PyFunctionRootNode -> PySuiteNode
        PySuiteNode funcCodeBlock = parseCodeBlock(suiteNode, initParamStatements, newFrameDescriptor);
        PyFunctionRootNode funcRootNode = new PyFunctionRootNode(funcCodeBlock);
        PyRootNode rootNode = new PyRootNode(funcRootNode, newFrameDescriptor);

        // Create a new UserFunction and a PyDefFuncNode defining this function
        PyUserFunction userFunction = new PyUserFunction(Truffle.getRuntime().createCallTarget(rootNode), args.size());
        PyDefFuncNode defFuncNode = new PyDefFuncNode(userFunction, functionName);
        return defFuncNode;
    }

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
    public static PyStatementNode parseStatement(SimpleParseTree simpleParseTree, FrameDescriptor frameDescriptor) {
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
                return parseFuncDef(simpleParseTree, frameDescriptor);
            }

            if (firstToken.getType() == Python3Parser.IF) {
                return parseIfStmt(simpleParseTree, frameDescriptor);
            }

            if (firstToken.getType() == Python3Parser.WHILE) {
                return parseWhileStmt(simpleParseTree, frameDescriptor);
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
                PyExpressionNode returnBody = parseExpression(simpleParseTree.getChild(1), frameDescriptor);
                PyReturnNode returnNode = new PyReturnNode(returnBody);
                return returnNode;
            }
        }

        if (simpleParseTree.getChildCount() >= 3) {
            if (simpleParseTree.isChildToken(1)) {
                Token secondToken = simpleParseTree.childAsToken(1);

                // Is this assignment? e.g.   x = 5 + 4
                if (secondToken.getType() == Python3Lexer.ASSIGN) {
                    PyExpressionNode varNameNode = parseExpression(simpleParseTree, 0, 0, frameDescriptor);

                    // Parsing a NAME will yield a ReadVarNode, but in this special case (lhs in assignment) we only
                    // care about the variable name (and do not want to read anything)
                    String varName = ((PyReadVarNode) varNameNode).getVarName();
                    PyExpressionNode assignValueNode = parseExpression(simpleParseTree,
                                                                       2,
                                                                       simpleParseTree.getChildCount() - 1,
                                                                       frameDescriptor);
                    FrameSlot slot = frameDescriptor.findOrAddFrameSlot(varName);
                    PyAssignNode assignNode = PyAssignNodeGen.create(assignValueNode, slot);

                    return assignNode;
                }
            }
            /*
             * augassign, e.g. x += 1
             */
            if (simpleParseTree.getChildPayload(1).equals(NODE_STR_AUGASSIGN)) {
                PyExpressionNode right = parseExpression(simpleParseTree.getChild(2), frameDescriptor);
                PyExpressionNode left = parseExpression(simpleParseTree.getChild(0), frameDescriptor);

                String varName = ((PyReadVarNode) left).getVarName();
                FrameSlot slot = frameDescriptor.findOrAddFrameSlot(varName);
                PyStatementNode assignStatement = null;

                switch (simpleParseTree.getChild(1).childAsToken(0).getType()) {
                    case Python3Parser.ADD_ASSIGN:
                        assignStatement = PyAddNodeGen.create(left, right);
                        break;
                    case Python3Parser.SUB_ASSIGN:
                        assignStatement = PySubtractNodeGen.create(left, right);
                        break;
                    case Python3Parser.MULT_ASSIGN:
                        assignStatement = PyMultiplyNodeGen.create(left, right);
                        break;
                    case Python3Parser.DIV_ASSIGN:
                        assignStatement = PyDivideNodeGen.create(left, right);
                        break;
                    case Python3Parser.MOD_ASSIGN:
                        assignStatement = PyModNodeGen.create(left, right);
                        break;
                    default:
                        throw new NotImplementedException("augassign not implemented: " + simpleParseTree.getChild(1)
                                .childAsToken(0)
                                .getType());
                }

                PyAssignNode assignNode = PyAssignNodeGen.create(assignStatement, slot);
                return assignNode;
            }
        }

        return parseExpression(simpleParseTree, 0, simpleParseTree.getChildCount() - 1, frameDescriptor);
    }

    public static PyExpressionNode parseToken(SimpleParseTree simpleParseTree, FrameDescriptor frameDescriptor) {
        assert simpleParseTree.isToken();

        if (simpleParseTree.isToken()) {
            if (simpleParseTree.asToken().getType() == Python3Lexer.DECIMAL_INTEGER) {
                try {
                    return new PyLongLitNode(Long.parseLong(simpleParseTree.asToken().getText()));
                } catch (NumberFormatException e) {
                    return new PyBigNumLitNode(simpleParseTree.asToken().getText());
                }
            }

            if (simpleParseTree.asToken().getType() == Python3Lexer.NAME) {
                FrameSlot slot = frameDescriptor.findOrAddFrameSlot(simpleParseTree.asToken()
                                                                            .getText());
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

            if (simpleParseTree.asToken().getType() == Python3Parser.NONE) {
                return new PyNoneLitNode();
            }

            if (simpleParseTree.asToken().getType() == Python3Parser.CONTINUE) {
                return new PyContinueNode();
            }

            if (simpleParseTree.asToken().getType() == Python3Parser.BREAK) {
                return new PyBreakNode();
            }
        }

        throw new NotImplementedException();
    }

    /**
     * Convenience method for parseExpression(SPT, from, to).
     */
    public static PyExpressionNode parseExpression(SimpleParseTree simpleParseTree, FrameDescriptor frameDescriptor) {
        return parseExpression(simpleParseTree, 0, simpleParseTree.getChildCount() - 1, frameDescriptor);
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
    public static PyExpressionNode parseExpression(SimpleParseTree simpleParseTree,
                                                   int fromIndex,
                                                   int toIndex,
                                                   FrameDescriptor frameDescriptor) {
        if (toIndex < fromIndex) {
            // This node does not have body - either a token or WTF
            if (simpleParseTree.isToken()) {
                return parseToken(simpleParseTree, frameDescriptor);
            }
            throw new NotImplementedException("No body for node, but node not Token.");
        }

        if (toIndex == fromIndex) {
            // This node has a single child - parse it as expression
            return parseExpression(simpleParseTree.getChild(toIndex),
                                   0,
                                   simpleParseTree.getChild(toIndex).getChildCount() - 1,
                                   frameDescriptor);
        }

        // Two body - function call?
        if (toIndex - fromIndex == 1) {
            // Check if first child is atom and its child a token
            if (simpleParseTree.getChild(0).getPayloadAsString().equals(NODE_STR_ATOM) &&
                    simpleParseTree.getChild(0).getChildCount() == 1 &&
                    simpleParseTree.getChild(0).isChildToken(0)) {
                String funcName = simpleParseTree.getChild(0).childAsToken(0).getText();
                PyReadVarNode funcNameNode = PyReadVarNodeGen.create(frameDescriptor.findOrAddFrameSlot(funcName));

                assert simpleParseTree.getChild(1).getPayloadAsString().equals(NODE_STR_TRAILER);

                SimpleParseTree arglistNode = simpleParseTree.getChild(1).getChild(1);

                PyFunctionCallNode callNode;

                if (arglistNode.getPayloadAsString().equals(NODE_STR_ARGLIST)) {
                    List<PyExpressionNode> arglist = parseArgList(arglistNode, frameDescriptor);
                    callNode = new PyFunctionCallNode(funcNameNode, arglist.toArray(new PyExpressionNode[arglist.size()]));
                } else {
                    callNode = new PyFunctionCallNode(funcNameNode, null);
                }

                return callNode;
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
                return parseExpression(simpleParseTree, 1, simpleParseTree.getChildCount() - 2, frameDescriptor);
            }

            if (simpleParseTree.isChildToken(toIndex - 1)) {

                PyExpressionNode right = parseExpression(simpleParseTree, toIndex, toIndex, frameDescriptor);
                PyExpressionNode left = parseExpression(simpleParseTree, fromIndex, toIndex - 2, frameDescriptor);

                switch (simpleParseTree.childAsToken(toIndex - 1).getType()) {
                    case Python3Lexer.ADD:
                        return PyAddNodeGen.create(left, right);
                    case Python3Lexer.MINUS:
                        return PySubtractNodeGen.create(left, right);
                    case Python3Lexer.STAR:
                        return PyMultiplyNodeGen.create(left, right);
                    case Python3Lexer.DIV:
                        return PyDivideNodeGen.create(left, right);
                    case Python3Lexer.MOD:
                        return PyModNodeGen.create(left, right);
                    case Python3Lexer.EQUALS:
                        // TODO
                        throw new NotImplementedException();
//                        binaryNode = new PyEqualsNode();
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
                PyExpressionNode right = parseExpression(simpleParseTree, toIndex, toIndex, frameDescriptor);
                PyExpressionNode left = parseExpression(simpleParseTree, fromIndex, toIndex - 2, frameDescriptor);

                switch (simpleParseTree.getChild(toIndex - 1).childAsToken(0).getType()) {
                    case Python3Parser.EQUALS:
                        return PyEqualsNodeGen.create(left, right);
                    case Python3Parser.NOT_EQ_1:
                    case Python3Parser.NOT_EQ_2:
                        return PyNotNodeGen.create(PyEqualsNodeGen.create(left, right));

                    case Python3Parser.LESS_THAN:
                        return PyLesserNodeGen.create(left, right);

                    case Python3Parser.LT_EQ:
                        return PyLesserEqualNodeGen.create(left, right);

                    case Python3Parser.GREATER_THAN:
                        return PyGreaterNodeGen.create(left, right);

                    case Python3Parser.GT_EQ:
                        return PyGreaterEqualNodeGen.create(left, right);

                    default:
                        throw new NotImplementedException("comp_op not implemented: " + simpleParseTree.getChild(toIndex - 1)
                                .childAsToken(0)
                                .getType());
                }
            }
        }
        throw new NotImplementedException("Got '" + simpleParseTree.getPayloadAsString() + "' from " + fromIndex + " to " + toIndex);
    }

    public static PySuiteNode parseCodeBlock(SimpleParseTree suiteNode, FrameDescriptor frameDescriptor) {
        return parseCodeBlock(suiteNode, null, frameDescriptor);
    }

    /**
     * Parse a suite subtree. Optionally provide list of nodes - if so, new nodes will be appended to the list.
     * This is useful for function calls - caller will pass list of read-write statement nodes to initialize
     * function parameters as local variables.
     */
    public static PySuiteNode parseCodeBlock(SimpleParseTree suiteNode,
                                             List<PyStatementNode> statementNodes,
                                             FrameDescriptor frameDescriptor) {
        assert suiteNode.getPayloadAsString().equals("suite");

        if (statementNodes == null) statementNodes = new ArrayList<>();

        for (SimpleParseTree child : suiteNode.getChildren()) {
            if (child.isToken()) {
                int tokenType = child.asToken().getType();
                if (tokenType == Python3Lexer.NEWLINE ||
                        tokenType == Python3Parser.INDENT ||
                        tokenType == Python3Parser.DEDENT)
                    continue;
            }

            statementNodes.add(parseStatement(child, frameDescriptor));
        }

        return new PySuiteNode(statementNodes.toArray(new PyStatementNode[statementNodes.size()]), false);
    }

    public static PySuiteNode parseFileInputBlock(SimpleParseTree fileInputNode, FrameDescriptor frameDescriptor) {
        assert fileInputNode.getPayloadAsString().equals(NODE_STR_FILE_INPUT);

        List<PyStatementNode> statementNodes = new ArrayList<>();
        for (SimpleParseTree child : fileInputNode.getChildren()) {
            if (child.isToken()) {
                int tokenType = child.asToken().getType();
                if (tokenType == Python3Lexer.NEWLINE || tokenType == Python3Parser.EOF)
                    continue;
            }

            PyStatementNode statementNode = parseStatement(child, frameDescriptor);
            statementNodes.add(statementNode);
        }

        // TODO do I really want to always return last result? Should I detect if I am in REPL mode?
        return new PySuiteNode(statementNodes.toArray(new PyStatementNode[statementNodes.size()]), true);
    }
}
