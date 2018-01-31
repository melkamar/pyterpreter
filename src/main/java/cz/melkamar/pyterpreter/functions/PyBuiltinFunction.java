package cz.melkamar.pyterpreter.functions;

import com.oracle.truffle.api.RootCallTarget;

public abstract class PyBuiltinFunction extends PyFunction {
    private String name;
    public PyBuiltinFunction(String name, RootCallTarget callTarget, int argc) {
        super(callTarget, argc);
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
