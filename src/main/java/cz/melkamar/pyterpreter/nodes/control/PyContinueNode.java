package cz.melkamar.pyterpreter.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.exceptions.PyContinueException;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;

public final class PyContinueNode extends PyExpressionNode {
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        throw PyContinueException.SINGLETON;
    }

    @Override
    public void print(int indent) {
        printIndented("CONTINUE", indent);
    }
}
