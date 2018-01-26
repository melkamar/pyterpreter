package cz.melkamar.pyterpreter.nodes.expr.arithmetic;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.nodes.PyBinaryNode;

import java.math.BigInteger;

@NodeInfo(shortName = "*")
public abstract class PyMultiplyNode extends PyBinaryNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    protected long multiply(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    @Specialization
    @TruffleBoundary
    protected BigInteger multiply(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Specialization(guards = "stringMultLong(left, right)")
    @TruffleBoundary
    protected String multiply(Object left, Object right) {
        // Todo python multiply strings
        throw new NotImplementedException();
    }

    protected boolean stringMultLong(Object a, Object b) {
        return a instanceof String && b instanceof Long;
    }

    @Override
    public void print(int indent) {
        printIndented("*", indent + 1);
    }
}
