package cz.melkamar.pyterpreter.functions;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.nodes.builtin.PyBuiltinNode;

public class PyBuiltinFunction extends PyFunction {
    private String name;
    public PyBuiltinFunction(String name, PyBuiltinNode targetNode) {
        super(Truffle.getRuntime().createCallTarget(new PyRootNode(targetNode, new FrameDescriptor())), targetNode.getArgCount());
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "<built-in function " + name + ">";
    }
}
