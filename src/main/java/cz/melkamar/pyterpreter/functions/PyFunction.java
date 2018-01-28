package cz.melkamar.pyterpreter.functions;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.utilities.CyclicAssumption;

public abstract class PyFunction {
    protected final String name;
    protected RootCallTarget callTarget;
    protected final CyclicAssumption targetNotRedefined;
//    private final String[] args;

    public PyFunction(String name, RootCallTarget callTarget) {
        this.name = name;
        this.callTarget = callTarget;
        this.targetNotRedefined = new CyclicAssumption(name);
//        this.args = args;
    }

    public String getName() {
        return name;
    }

    public RootCallTarget getCallTarget() {
        return callTarget;
    }

    public void setCallTarget(RootCallTarget callTarget) {
        this.callTarget = callTarget;
        targetNotRedefined.invalidate();
    }

    public Assumption getTargetNotRedefined() {
        return targetNotRedefined.getAssumption();
    }
}
