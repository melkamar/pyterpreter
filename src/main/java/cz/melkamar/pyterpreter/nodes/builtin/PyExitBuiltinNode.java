package cz.melkamar.pyterpreter.nodes.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.exceptions.SystemExitException;

public class PyExitBuiltinNode extends PyBuiltinNode {
    public PyExitBuiltinNode() {
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        throw new SystemExitException();
    }

    @Override
    public void print(int indent) {
        printIndented("EXIT", indent);
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
