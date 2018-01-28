package cz.melkamar.pyterpreter.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.exceptions.ReturnException;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;
import cz.melkamar.pyterpreter.nodes.PyStatementNode;

public final class PyReturnNode extends PyStatementNode{
    @Child private PyExpressionNode valueNode;

    public PyReturnNode(PyExpressionNode valueNode) {
        this.valueNode = valueNode;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        if (valueNode != null){
            Object result = valueNode.executeGeneric(frame);
            throw new ReturnException(result);
        }
        else throw new ReturnException(null);
    }

    @Override
    public void print(int indent) {
        printIndented("RETURN", indent);
        valueNode.print(indent+1);
    }
}
