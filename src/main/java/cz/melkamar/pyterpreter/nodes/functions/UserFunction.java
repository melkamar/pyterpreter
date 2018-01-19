package cz.melkamar.pyterpreter.nodes.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.template.PyNode;

public class UserFunction extends Function {
    public UserFunction(String[] args) {
        super(args);
    }

    @Override
    public Object execute(Environment env) {
        for (PyNode child : body) {
            Object lastRes = child.execute(env);
            if (env.isReturnFlag()) return lastRes;
        }
        return null;
    }
}
