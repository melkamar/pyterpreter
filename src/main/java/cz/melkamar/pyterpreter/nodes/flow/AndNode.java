package cz.melkamar.pyterpreter.nodes.flow;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.TypeCast;
import cz.melkamar.pyterpreter.nodes.PyNode;

public class AndNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        for (PyNode child : children) {
            if (!TypeCast.castToBoolean(child.execute(env))) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AND";
    }
}
