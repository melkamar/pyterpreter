package cz.melkamar.pyterpreter.nodes.builtin.typecast;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import cz.melkamar.pyterpreter.nodes.builtin.PyBuiltinNode;
import cz.melkamar.pyterpreter.nodes.function.PyReadArgNode;

public class PyStrOldBuiltinNode extends PyBuiltinNode {
    @Node.Child
    private PyReadArgNode textArg;

    public PyStrOldBuiltinNode() {
        textArg = new PyReadArgNode(0);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return textArg.executeGeneric(frame).toString();
    }

    @Override
    public void print(int indent) {
        printIndented("STR", indent);
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
