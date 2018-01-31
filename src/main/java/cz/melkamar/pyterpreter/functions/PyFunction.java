package cz.melkamar.pyterpreter.functions;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.MaterializedFrame;

public abstract class PyFunction {
    /**
     * Truffle call target, created from a {@link cz.melkamar.pyterpreter.nodes.PyRootNode}, e.g.
     *
     * callTarget = Truffle.getRuntime().createCallTarget(rootNode)
     */
    protected RootCallTarget callTarget;
    protected MaterializedFrame scope;
    public final int argc;

    public PyFunction(RootCallTarget callTarget, int argc) {
        this.callTarget = callTarget;
        this.argc = argc;
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

