package cz.melkamar.pyterpreter.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;
import cz.melkamar.pyterpreter.TypeCast;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;


public class PyAndNode extends PyExpressionNode {
    @Child
    private PyExpressionNode left;
    @Child
    private PyExpressionNode right;
    private final ConditionProfile evaluateRightProfile = ConditionProfile.createCountingProfile();

    public PyAndNode(PyExpressionNode left, PyExpressionNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return executeBoolean(frame);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frame) {
        boolean leftVal;
        boolean rightVal = false;

        try {
            leftVal = left.executeBoolean(frame);
        } catch (UnexpectedResultException e) {
            leftVal = TypeCast.castToBoolean(e.getResult());
        }

        if (evaluateRightProfile.profile(leftVal)) {
            try {
                rightVal = right.executeBoolean(frame);
            } catch (UnexpectedResultException e) {
                rightVal = TypeCast.castToBoolean(e.getResult());
            }
        }

        return leftVal && rightVal;
    }

    @Override
    public void print(int indent) {
        printIndented("AND", indent);
        left.print(indent + 1);
        right.print(indent + 1);
    }
}
