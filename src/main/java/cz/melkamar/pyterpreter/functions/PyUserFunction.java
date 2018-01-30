package cz.melkamar.pyterpreter.functions;

import com.oracle.truffle.api.RootCallTarget;

public class PyUserFunction extends PyFunction {
    public PyUserFunction(RootCallTarget callTarget) {
        super(callTarget);
    }

    @Override
    public String toString() {
        return "<function " + this.hashCode() + ">";
    }
}
