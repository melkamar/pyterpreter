package cz.melkamar.pyterpreter.nodes.builtin.typecast;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import cz.melkamar.pyterpreter.nodes.builtin.PyBuiltinNode;
import cz.melkamar.pyterpreter.nodes.function.PyReadArgNode;

@NodeChild(value = "argNode", type = PyReadArgNode.class)
public abstract class PyStrBuiltinNode extends PyBuiltinNode {
    @Specialization
    public String convertString(String toConvert) {
        return toConvert;
    }

    @Specialization
    public String convertLong(long toConvert) {
        return String.valueOf(toConvert);
    }

    @Specialization
    public String convertBool(boolean toConvert) {
        if (toConvert) return "True";
        else return "False";
    }

    @Specialization
    public String convertGeneric(Object toConvert) {
        return toConvert.toString();
    }


    @Override
    public void print(int indent) {
        printIndented("STR", indent);
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
