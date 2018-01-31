package cz.melkamar.pyterpreter.nodes.expr.compare;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import cz.melkamar.pyterpreter.functions.PyFunction;
import cz.melkamar.pyterpreter.nodes.PyBinaryNode;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;
import cz.melkamar.pyterpreter.types.PyNoneType;

import java.math.BigInteger;

public abstract class PyEqualsNode extends PyBinaryNode {
    @Specialization
    protected boolean equal(long left, long right) {
        return left == right;
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected boolean equal(BigInteger left, BigInteger right) {
        return left.equals(right);
    }

    @Specialization
    protected boolean equal(boolean left, boolean right) {
        return left == right;
    }

    @Specialization
    protected boolean equal(String left, String right) {
        return left.equals(right);
    }

    @Specialization
    protected boolean equal(PyFunction left, PyFunction right) {
        return left == right;
    }

    @Specialization
    protected boolean equal(PyNoneType left, PyNoneType right) {
        return left == right;
    }

    @Specialization(guards = "left.getClass() != right.getClass()")
    protected boolean equal(Object left, Object right) {
        assert !left.equals(right);
        return false;
    }

    protected abstract PyExpressionNode getLeftChild();
    protected abstract PyExpressionNode getRightChild();
    @Override
    public void print(int indent) {
        printIndented("==", indent);
        getLeftChild().print(indent+1);
        getRightChild().print(indent+1);
    }
}
