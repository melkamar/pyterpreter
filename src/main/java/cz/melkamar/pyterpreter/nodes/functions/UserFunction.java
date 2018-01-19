package cz.melkamar.pyterpreter.nodes.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.template.PyNode;

import java.util.Arrays;

public class UserFunction extends Function {
    public UserFunction(String[] args) {
        super(args);
    }

    @Override
    public Object execute(Environment env) {
        for (PyNode child : children) {
            Object lastRes = child.execute(env);
            if (env.isReturnFlag()) return lastRes;
        }
        return null;
    }

    @Override
    public void print(int indent) {
        printIndented("userfunc " + " " + Arrays.toString(args), indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }
}
