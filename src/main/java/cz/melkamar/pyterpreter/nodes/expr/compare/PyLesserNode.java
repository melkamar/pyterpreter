package cz.melkamar.pyterpreter.nodes.expr.compare;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import cz.melkamar.pyterpreter.nodes.PyBinaryNode;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;

import java.math.BigInteger;

public abstract class PyLesserNode extends PyBinaryNode {
    @Specialization
    protected boolean greater(long left, long right) {
        return left < right;
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected boolean greater(BigInteger left, BigInteger right) {
        return left.compareTo(right) < 0;
    }

    @Specialization
    protected boolean greater(String left, String right) {
        return left.compareTo(right) < 0;
    }

    @Specialization(guards = "left.getClass() != right.getClass()")
    protected boolean greater(Object left, Object right) {
        assert !left.equals(right);
        return false;
    }

    protected abstract PyExpressionNode getLeftChild();
    protected abstract PyExpressionNode getRightChild();
    @Override
    public void print(int indent) {
        printIndented("<", indent);
        getLeftChild().print(indent+1);
        getRightChild().print(indent+1);
    }
}
