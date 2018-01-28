package cz.melkamar.pyterpreter.functions;

import com.oracle.truffle.api.RootCallTarget;

public class PyUserFunction extends PyFunction {
    public PyUserFunction(String name, RootCallTarget callTarget) {
        super(name, callTarget);
    }

    @Override
    public String toString() {
        return "<function " + name + ">";
    }
}
