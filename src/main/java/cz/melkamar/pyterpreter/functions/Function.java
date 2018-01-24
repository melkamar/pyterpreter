package cz.melkamar.pyterpreter.functions;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.MaterializedFrame;
import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyNode;

import java.util.ArrayList;
import java.util.List;

public abstract class Function {
//    public final RootCallTarget callTarget;
//    private MaterializedFrame scope;

    protected String[] args;
    protected List<PyNode> body;

//    public Function(RootCallTarget callTarget) {
//        this.args = new String[0];
//        body = new ArrayList<>();
//        this.callTarget = callTarget;
//    }

//    public Function(String[] args, RootCallTarget callTarget) {
//        this.args = args;
//        body = new ArrayList<>();
//        this.callTarget = callTarget;
//    }

    public Function() {
        this.args = new String[0];
        body = new ArrayList<>();
    }

    public Function(String[] args) {
        this.args = args;
        body = new ArrayList<>();
    }

//    public MaterializedFrame getScope() {
//        return scope;
//    }
//
//    public void setScope(MaterializedFrame scope) {
//        this.scope = scope;
//    }

    public String[] getArgNames() {
        return args;
    }

    public abstract Object execute(Environment env);
}
