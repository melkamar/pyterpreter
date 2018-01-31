package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class PyStatementNode extends PyBaseNode {
    public abstract Object executeGeneric(VirtualFrame frame);
}
