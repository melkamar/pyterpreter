package cz.melkamar.pyterpreter.functions;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.MaterializedFrame;

public abstract class PyFunction {
    protected RootCallTarget callTarget;
    protected MaterializedFrame scope;
//    private final String[] args;

    public PyFunction(RootCallTarget callTarget) {
        this.callTarget = callTarget;
//        this.args = args;
    }

    public RootCallTarget getCallTarget() {
        return callTarget;
    }

    public MaterializedFrame getScope() {
        return scope;
    }

    public void setScope(MaterializedFrame scope) {
        this.scope = scope;
    }
}

