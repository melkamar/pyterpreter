package cz.melkamar.pyterpreter.nodes.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.template.PyNode;

import java.util.Arrays;

public class FunctionNode extends PyNode {
    private String[] args;

    public FunctionNode(String[] args) {
        this.args = args;
    }

    public String[] getArgNames() {
        return args;
    }

    @Override
    public Object execute(Environment env) {
        for (PyNode child : children) {
            child.execute(env);
        }
        return null; // todo co tu vracet? podle returnu v metodě
    }

    @Override
    public void print(int indent) {
        printIndented("func " + " " + Arrays.toString(args), indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }
}
