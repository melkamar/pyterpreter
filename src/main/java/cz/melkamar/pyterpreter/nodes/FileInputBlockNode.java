package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;

/**
 * Very similar to {@link cz.melkamar.pyterpreter.functions.CodeBlockNode}, but this returns the result of last
 * statement - for REPL.
 */
public class FileInputBlockNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        Object lastRes = null;
        for (PyNode child : children) {
            lastRes = child.execute(env);
            if (env.isReturnFlag()) return lastRes;
        }
        return lastRes;
    }

    @Override
    public void print(int indent) {
        printIndented("code", indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }
}
