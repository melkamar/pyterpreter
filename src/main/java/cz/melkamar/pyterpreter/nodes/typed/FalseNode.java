package cz.melkamar.pyterpreter.nodes.typed;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyNode;

public class FalseNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        return false;
    }

    @Override
    public String toString() {
        return "False";
    }
}
