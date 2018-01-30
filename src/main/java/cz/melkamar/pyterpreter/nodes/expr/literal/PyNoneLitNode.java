package cz.melkamar.pyterpreter.nodes.expr.literal;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.types.PyNoneType;

public class PyNoneLitNode extends PyLiteralNode {
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return PyNoneType.NONE_SINGLETON;
    }

    @Override
    public void print(int indent) {
        printIndented("None", indent);
    }
}
