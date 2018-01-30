package cz.melkamar.pyterpreter.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.exceptions.PyBreakException;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;

public final class PyBreakNode extends PyExpressionNode {
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        throw PyBreakException.SINGLETON;
    }

    @Override
    public void print(int indent) {
        printIndented("BREAK", indent);
    }
}
