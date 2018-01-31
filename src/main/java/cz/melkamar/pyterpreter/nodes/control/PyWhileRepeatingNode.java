package cz.melkamar.pyterpreter.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RepeatingNode;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.BranchProfile;
import cz.melkamar.pyterpreter.TypeCast;
import cz.melkamar.pyterpreter.exceptions.PyBreakException;
import cz.melkamar.pyterpreter.exceptions.PyContinueException;
import cz.melkamar.pyterpreter.nodes.PyBaseNode;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;
import cz.melkamar.pyterpreter.nodes.PyStatementNode;

public final class PyWhileRepeatingNode extends PyBaseNode implements RepeatingNode {
    @Node.Child
    private PyExpressionNode conditionNode;
    @Node.Child
    private PyStatementNode bodyNode;

    private final BranchProfile continueTaken = BranchProfile.create();
    private final BranchProfile breakTaken = BranchProfile.create();

    public PyWhileRepeatingNode(PyExpressionNode conditionNode, PyStatementNode bodyNode) {
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
    }

    @Override
    public boolean executeRepeating(VirtualFrame frame) {
        if (!evaluateCondition(frame)) return false;
        try {
            bodyNode.executeGeneric(frame);
            return true;
        } catch (PyContinueException e) {
            continueTaken.enter();
            return true;
        } catch (PyBreakException e) {
            breakTaken.enter();
            return false;
        }
    }

    private boolean evaluateCondition(VirtualFrame frame) {
        try {
            return conditionNode.executeBoolean(frame);
        } catch (UnexpectedResultException e) {
            return TypeCast.castToBoolean(e.getResult());
        }
    }

    @Override
    public void print(int indent) {
        printIndented("WHILE", indent);
        conditionNode.print(indent+1);
        bodyNode.print(indent+1);
    }
}
