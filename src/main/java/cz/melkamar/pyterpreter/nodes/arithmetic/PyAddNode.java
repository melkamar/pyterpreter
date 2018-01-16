package cz.melkamar.pyterpreter.nodes.arithmetic;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.nodes.template.PyNode;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 16.01.2018 16:43.
 */
public class PyAddNode extends PyNode {
    @Override
    public void addChild(PyNode node) {
        super.addChild(node);
        assert children.size() <= 2;
    }

    @Override
    public Object execute(Environment env) {
        throw new NotImplementedException();
    }

    @Override
    public void print(int indent) {
        printIndented("+", indent);
        for (PyNode child : children) {
            child.print(indent+1);
        }
    }
}
