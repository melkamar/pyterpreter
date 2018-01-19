package cz.melkamar.pyterpreter.nodes.functions;

import cz.melkamar.pyterpreter.nodes.template.PyNode;

public abstract class Function extends PyNode {
    protected String[] args;

    public Function(String[] args) {
        this.args = args;
    }

    public String[] getArgNames() {
        return args;
    }
}
