package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.template.PyNode;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 16.01.2018 20:58.
 */
public class AssignNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        assert children.size() == 2;

        String varName = ((PySymbolNode) children.get(0)).name;
        Object rightChildResult = children.get(1).execute(env);

        PySymbolNode symbolNode = new PySymbolNode(varName);
        env.putValue(symbolNode.name, rightChildResult);
        return null;
    }

    @Override
    public void print(int indent) {
        printIndented(":=", indent);
        for (PyNode child : children) {
            child.print(indent+1);
        }
    }
}
