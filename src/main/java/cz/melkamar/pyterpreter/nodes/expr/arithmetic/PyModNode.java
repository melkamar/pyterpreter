package cz.melkamar.pyterpreter.nodes.expr.arithmetic;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.melkamar.pyterpreter.nodes.PyBinaryNode;

import java.math.BigInteger;

@NodeInfo(shortName = "%")
public abstract class PyModNode extends PyBinaryNode {
    @Specialization
    protected long modulo(long left, long right) {
        return Math.floorMod(left, right);
    }

    @Specialization
    @TruffleBoundary
    protected BigInteger modulo(BigInteger left, BigInteger right) {
        return left.mod(right);
    }

    @Override
    public void print(int indent) {
        printIndented("%", indent + 1);
    }
}
