package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

public abstract class PyStatementNode extends Node {
    public abstract Object executeGeneric(VirtualFrame frame);
}
