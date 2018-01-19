package cz.melkamar.pyterpreter.nodes.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.template.PyNode;

/**
 * Node for handling return statements. When executed, it will set environment "return" flag.
 * The function executing the AST will check for this flag after every statement and if it is set,
 * returns last value obtained.
 */
public class ReturnNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        assert children.size() == 1;
        env.setReturnFlag();
        return children.get(0).execute(env);
    }

    @Override
    public void print(int indent) {
        printIndented("return", indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }
}
