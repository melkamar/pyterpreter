package cz.melkamar.pyterpreter.functions.builtin;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.nodes.RootNode;
import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.functions.PyBuiltinFunction;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.nodes.builtin.PyPrintBuiltinNode;

public class PyPrintBuiltinFunction extends PyBuiltinFunction {

    public PyPrintBuiltinFunction(Environment environment) {
        super("print", null, 1);
        callTarget = createCallTarget(environment);
    }

    private RootCallTarget createCallTarget(Environment environment){
        PyPrintBuiltinNode funcNode = new PyPrintBuiltinNode(environment.getStdout());
        RootNode rootNode = new PyRootNode(funcNode, new FrameDescriptor());
        RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
        return callTarget;
    }
}
