package cz.melkamar.pyterpreter.nodes.flow;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.TypeCast;
import cz.melkamar.pyterpreter.nodes.PyNode;

public class NotNode extends PyNode {
    @Override
    public Object execute(Environment env){
        assert children.size() == 1;
        return !TypeCast.castToBoolean(children.get(0).execute(env));
    }

    @Override
    public String toString() {
        return "NOT";
    }
}
