package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 15.01.2018 21:01.
 */
public class PySymbolNode extends PyNode{
    public String name;

    public PySymbolNode(String name) {
        this.name = name;
    }

    @Override
    public Object execute(Environment env) {
        return env.getValue(this.name);
    }

    @Override
    public void print(int indent) {
        printIndented("{" + this.name+"}", indent);
    }
}
