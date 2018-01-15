package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.template.PyNode;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 05.11.2017 22:15.
 */
public class PyNumberNode extends PyNode {
    private final long number;

    public PyNumberNode(long number) {
        this.number = number;
    }

    @Override
    public Object execute(Environment env) {
        return this.number;
    }
}
