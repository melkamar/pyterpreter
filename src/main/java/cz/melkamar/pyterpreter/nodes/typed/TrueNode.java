package cz.melkamar.pyterpreter.nodes.typed;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyNode;

public class TrueNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        return true;
    }

    @Override
    public String toString() {
        return "True";
    }
}
