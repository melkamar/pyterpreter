package cz.melkamar.pyterpreter.nodes.flow;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyNode;

public class IfNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        PyNode testNode = children.get(0);
        PyNode ifTrueNode = children.get(1);
        PyNode ifFalseNode = null;
        if (children.size() > 2) ifFalseNode = children.get(2);

        Boolean testResult = (Boolean) testNode.execute(env);
        Object statementResult = null;
        if (testResult) {
            statementResult = ifTrueNode.execute(env);
        } else {
            if (ifFalseNode != null) statementResult = ifFalseNode.execute(env);
        }
        if (env.isReturnFlag()) return statementResult;
        else return null;
    }

    @Override
    public void print(int indent) {
        printIndented("IF", indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }
}
