package cz.melkamar.pyterpreter.functions.builtin;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.functions.PyBuiltinFunction;
import cz.melkamar.pyterpreter.nodes.builtin.PyBuiltinNode;
import cz.melkamar.pyterpreter.nodes.builtin.PyPrintBuiltinNode;

public class PyPrintBuiltinFunction extends PyBuiltinFunction {
    public PyPrintBuiltinFunction(Environment environment) {
        super("print", 1, environment);
    }


    @Override
    protected PyBuiltinNode createBuiltinNode(Environment environment) {
        return new PyPrintBuiltinNode(environment.getStdout());
    }
}
