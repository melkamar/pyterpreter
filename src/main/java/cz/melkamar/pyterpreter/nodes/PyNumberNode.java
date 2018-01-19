package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 05.11.2017 22:15.
 */
public class PyNumberNode extends PyNode {
    public final long number;

    public PyNumberNode(Long number) {
        this.number = number;
    }

    public PyNumberNode(String string){
        this.number = Long.parseLong(string);
    }

    @Override
    public Object execute(Environment env) {
        return this.number;
    }

    @Override
    public void print(int indent) {
        printIndented(number+"", indent);
        for (PyNode child : children) {
            child.print(indent+1);
        }
    }
}
