package cz.melkamar.pyterpreter.parser;

import cz.melkamar.pyterpreter.antlr.Python3Lexer;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.nodes.AssignNode;
import cz.melkamar.pyterpreter.nodes.PyFunctionNode;
import cz.melkamar.pyterpreter.nodes.PyNumberNode;
import cz.melkamar.pyterpreter.nodes.PySymbolNode;
import cz.melkamar.pyterpreter.nodes.arithmetic.PyAddNode;
import cz.melkamar.pyterpreter.nodes.arithmetic.PySubtractNode;
import cz.melkamar.pyterpreter.nodes.template.PyNode;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AtomParserHelper {
    final static String NODE_STR_TERM = "term";
    final static String NODE_STR_EXPR_STAR = "testlist_star_expr";

    public static PyNode parseFuncDef(SimpleParseTree simpleParseTree, PyNode currentPyNode) {
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
                throw new AssertionError("multiple-argument func " +
                                                 "unexpected node: " + String.valueOf(arg.getPayload()));
            }
        }

        PyNode funcNode = new PyFunctionNode(functionName,
                                             Arrays.copyOf(args.toArray(), args.toArray().length, String[].class));
        // TODO code

        System.out.println("Defining function '" + functionName + "' with args " + Arrays.toString(
                args.toArray()));

        return funcNode;
    }

    public static PyNode parseStatement(SimpleParseTree simpleParseTree, PyNode currentPyNode) {
        if (simpleParseTree.getChildCount() == 0) {
//            return parseTermNode(simpleParseTree.children.get(0));
            throw new NotImplementedException();
        }

        if (simpleParseTree.isChildToken(0)) {
            Token firstToken = simpleParseTree.pstrChildAsToken(0);

            // Defining a function?
            if (firstToken.getType() == Python3Lexer.DEF) {
                PyNode defPyNode = parseFuncDef(simpleParseTree, currentPyNode);
                currentPyNode.addChild(defPyNode);
                return defPyNode;
            }
        }

        if (simpleParseTree.getChildCount() >= 3) {
            if (simpleParseTree.isChildToken(1)) {
                Token secondToken = simpleParseTree.pstrChildAsToken(1);

                // Is this assignment? e.g.   x = 5 + 4
                if (secondToken.getType() == Python3Lexer.ASSIGN) {
//                    String variableName = simpleParseTree.pstrChildAsToken(0).getText();
                    PyNode varNameNode = parseExpression(simpleParseTree, 0, 0);

                    AssignNode assignNode = new AssignNode();
                    PyNode assignExpression = parseExpression(simpleParseTree,
                                                                        2,
                                                                        simpleParseTree.getChildCount() - 1);
                    assignNode.addChild(varNameNode);
                    assignNode.addChild(assignExpression);
                    return assignNode;
                }
            }
        }

        return parseExpression(simpleParseTree, 0, simpleParseTree.getChildCount() - 1);
    }

    public static PyNode parseToken(SimpleParseTree simpleParseTree) {
        if (simpleParseTree.isToken()) {
            if (simpleParseTree.asToken().getType() == Python3Lexer.DECIMAL_INTEGER) {
                return new PyNumberNode(Long.parseLong(simpleParseTree.asToken().getText()));
            }

            if (simpleParseTree.asToken().getType() == Python3Lexer.NAME) {
                return new PySymbolNode(simpleParseTree.asToken().getText());
            }
        }

        throw new NotImplementedException();
    }

    public static PyNode parseExpression(SimpleParseTree simpleParseTree, int fromIndex, int toIndex) {
        if (toIndex < fromIndex) return null;
        if (toIndex == fromIndex) {
            SimpleParseTree child = simpleParseTree.getChild(toIndex);
            if (child.isToken()){
                return parseToken(child);
            }
            switch (String.valueOf(child.getPayload())) {
                case NODE_STR_TERM:
                    return parseTermNode(child);
                case NODE_STR_EXPR_STAR:
                    return parseExpression(child, 0, child.getChildCount() - 1);
                default:
                    throw new NotImplementedException();
            }
        }

        // Binary operation?
        if (toIndex - fromIndex >= 2) { // At least three children
            if (simpleParseTree.isChildToken(toIndex - 1)) {

                PyNode aritNode = null;
                switch (simpleParseTree.pstrChildAsToken(toIndex - 1).getType()) {
                    case Python3Lexer.ADD:
                        aritNode = new PyAddNode();
                        break;
                    case Python3Lexer.MINUS:
                        aritNode = new PySubtractNode();
                        break;
                    default:
                        throw new NotImplementedException();
                }

                PyNode right = parseTermNode(simpleParseTree.getChild(toIndex));
                PyNode left = parseExpression(simpleParseTree, fromIndex, toIndex - 2);

                aritNode.addChild(left);
                aritNode.addChild(right);
                return aritNode;

            }
        }
        throw new NotImplementedException();
    }

    public static PyNode parseTermNode(SimpleParseTree simpleParseTree) {
        if (simpleParseTree.getChildCount() == 1 && simpleParseTree.isChildToken(0)) {
            if (simpleParseTree.pstrChildAsToken(0).getType() == Python3Lexer.DECIMAL_INTEGER) {
                return new PyNumberNode(Long.parseLong(simpleParseTree.pstrChildAsToken(0).getText()));
            }

            if (simpleParseTree.pstrChildAsToken(0).getType() == Python3Lexer.NAME) {
                return new PySymbolNode(simpleParseTree.pstrChildAsToken(0).getText());
            }
        }
        throw new NotImplementedException();
    }
}
