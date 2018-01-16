package cz.melkamar.pyterpreter.nodes.template;

import cz.melkamar.pyterpreter.Environment;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 03.11.2017 10:03.
 */
public class PyRootNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        return null;
    }

    @Override
    public void print(int indent) {
        printIndented("ROOT", indent);

        for (PyNode child : children) {
            child.print(indent+1);
        }
    }

    public void print(){
        print(0);
    }
}
