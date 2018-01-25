package cz.melkamar.pyterpreter.nodes.typed;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyNode;

public class FalseNode extends PyNode {
    @Override
    public Object execute(VirtualFrame frame) {
        return false;
    }

    @Override
    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return false;
    }

    @Override
    public String toString() {
        return "False";
    }
}
