package cz.melkamar.pyterpreter.functions;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.nodes.RootNode;
import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.nodes.builtin.PyBuiltinNode;

public abstract class PyBuiltinFunction extends PyFunction {
    private String name;
    public PyBuiltinFunction(String name, int argc, Environment environment) {
        super(null, argc);
        this.name = name;
        callTarget = createCallTarget(environment);
    }

    public String getName() {
        return name;
    }

    protected RootCallTarget createCallTarget(Environment environment){
        PyBuiltinNode funcNode = createBuiltinNode(environment);
        RootNode rootNode = new PyRootNode(funcNode, new FrameDescriptor());
        RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
        return callTarget;
    }

    protected abstract PyBuiltinNode createBuiltinNode(Environment environment);

    @Override
    public String toString() {
        return "<built-in function " + name + ">";
    }
}
