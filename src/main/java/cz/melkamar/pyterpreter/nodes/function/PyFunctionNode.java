package cz.melkamar.pyterpreter.nodes.function;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.nodes.PyStatementNode;

public class PyFunctionNode extends PyRootNode {
    public PyFunctionNode(PyStatementNode child,
                          FrameDescriptor frameDescriptor) {
        super(child, frameDescriptor);
    }
}
