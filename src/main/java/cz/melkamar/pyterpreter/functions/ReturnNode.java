package cz.melkamar.pyterpreter.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.nodes.PyNode;

/**
 * Node for handling return statements. When executed, it will set environment "return" flag.
 * The function executing the AST will check for this flag after every statement and if it is set,
 * returns last value obtained.
 */
public class ReturnNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        assert children.size() == 1 || children.size() == 0;
        env.setReturnFlag();
        if (children.size() == 1) return children.get(0).execute(env);
        else if (children.size() == 0) return null;
        throw new NotImplementedException();
    }

    @Override
    public void print(int indent) {
        printIndented("return", indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }
}
