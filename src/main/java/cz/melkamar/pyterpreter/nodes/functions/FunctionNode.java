package cz.melkamar.pyterpreter.nodes.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.nodes.template.PyNode;

import java.util.Arrays;

public class FunctionNode extends PyNode {
    private String[] args;

    public FunctionNode(String[] args) {
        this.args = args;
    }

    @Override
    public Object execute(Environment env) {
        throw new NotImplementedException();
    }

    @Override
    public void print(int indent) {
        printIndented("func " + " "+ Arrays.toString(args), indent);
        for (PyNode child : children) {
            child.print(indent+1);
        }
    }
}
