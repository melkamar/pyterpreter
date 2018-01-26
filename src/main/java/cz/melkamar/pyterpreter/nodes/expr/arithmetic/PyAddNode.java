package cz.melkamar.pyterpreter.nodes.expr.arithmetic;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.melkamar.pyterpreter.nodes.PyBinaryNode;

import java.math.BigInteger;

@NodeInfo(shortName = "+")
public abstract class PyAddNode extends PyBinaryNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    protected long add(long left, long right) {
        return Math.addExact(left, right);
    }

    @Specialization
    @TruffleBoundary
    protected BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Specialization(guards = "isString(left, right)")
    @TruffleBoundary
    protected String add(Object left, Object right) {
        return left.toString() + right.toString();
    }

    /**
     * Guard for String concatenation: returns true if either the left or the right operand is a
     * {@link String}.
     */
    protected boolean isString(Object a, Object b) {
        return a instanceof String || b instanceof String;
    }

    @Override
    public void print(int indent) {
        printIndented("+", indent+1);
    }
}
