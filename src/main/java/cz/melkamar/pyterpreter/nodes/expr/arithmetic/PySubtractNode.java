package cz.melkamar.pyterpreter.nodes.expr.arithmetic;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.melkamar.pyterpreter.nodes.PyBinaryNode;

import java.math.BigInteger;

@NodeInfo(shortName = "-")
public abstract class PySubtractNode extends PyBinaryNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    protected long subtract(long left, long right) {
        return Math.subtractExact(left, right);
    }

    @Specialization
    @TruffleBoundary
    protected BigInteger subtract(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public void print(int indent) {
        printIndented("-", indent + 1);
    }
}
