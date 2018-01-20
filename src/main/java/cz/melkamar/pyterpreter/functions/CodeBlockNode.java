package cz.melkamar.pyterpreter.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyNode;

public class CodeBlockNode extends PyNode {
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
        printIndented("code", indent);
        for (PyNode child : children) {
            child.print(indent+1);
        }
    }
}
