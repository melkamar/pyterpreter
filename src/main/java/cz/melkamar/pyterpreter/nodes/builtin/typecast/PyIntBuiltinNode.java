package cz.melkamar.pyterpreter.nodes.builtin.typecast;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import cz.melkamar.pyterpreter.nodes.builtin.PyBuiltinNode;
import cz.melkamar.pyterpreter.nodes.function.PyReadArgNode;

import java.math.BigInteger;

@NodeChild(value = "argNode", type = PyReadArgNode.class)
public abstract class PyIntBuiltinNode extends PyBuiltinNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    public long convertString(String toConvert) {
        BigInteger bigInteger = new BigInteger(toConvert);
        return bigInteger.longValueExact();
    }

    @Specialization(replaces = "convertString")
    public BigInteger convertStringBig(String toConvert) {
        return new BigInteger(toConvert);
    }

    @Specialization
    public long convertLong(long toConvert) {
        return toConvert;
    }

    @Specialization
    public long convertBool(boolean toConvert) {
        if (toConvert) return 1;
        else return 0;
    }

    @Specialization
    public BigInteger convertGeneric(Object toConvert) {
        return new BigInteger(toConvert.toString());
    }


    @Override
    public void print(int indent) {
        printIndented("INT", indent);
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
