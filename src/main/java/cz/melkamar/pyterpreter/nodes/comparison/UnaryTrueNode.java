package cz.melkamar.pyterpreter.nodes.comparison;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.nodes.PyNode;

/**
 * Node for testing if single expression evaluates to true, e.g.
 * if x:
 *     print("true")
 *
 */
public class UnaryTrueNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        return new NotImplementedException();
    }

    @Override
    public void print(int indent) {
        printIndented("isTrue?", indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }
}
