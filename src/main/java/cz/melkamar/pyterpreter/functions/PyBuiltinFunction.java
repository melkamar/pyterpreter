package cz.melkamar.pyterpreter.functions;

import com.oracle.truffle.api.RootCallTarget;

public class PyBuiltinFunction extends PyFunction {
    public PyBuiltinFunction(String name, RootCallTarget callTarget) {
        super(name, callTarget);
    }

    @Override
    public String toString() {
        return "<built-in function " + name + ">";
    }
}
