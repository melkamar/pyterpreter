package cz.melkamar.pyterpreter.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyNode;

import java.util.ArrayList;
import java.util.List;

public abstract class Function {
    protected String[] args;
    protected List<PyNode> body;

    public Function(String[] args) {
        this.args = args;
        body = new ArrayList<>();
    }

    public String[] getArgNames() {
        return args;
    }

    public abstract Object execute(Environment env);
}
