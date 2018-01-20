package cz.melkamar.pyterpreter.nodes.comparison;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.BinaryNode;
import cz.melkamar.pyterpreter.nodes.PyNode;

import java.util.Objects;

public class EqualsNode extends BinaryNode {
    @Override
    public Object execute(Environment env) {
        Object leftChildResult = children.get(0).execute(env);
        Object rightChildResult = children.get(1).execute(env);

//        if (leftChildResult instanceof Long && rightChildResult instanceof Long)
            return Objects.equals(leftChildResult, rightChildResult);

//        throw new NotImplementedException();
    }

    @Override
    public void print(int indent) {
        printIndented("==", indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }
}
