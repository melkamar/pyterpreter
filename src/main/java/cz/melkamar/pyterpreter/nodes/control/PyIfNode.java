package cz.melkamar.pyterpreter.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;
import cz.melkamar.pyterpreter.TypeCast;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;
import cz.melkamar.pyterpreter.nodes.PyStatementNode;

public final class PyIfNode extends PyStatementNode {
    @Child private PyExpressionNode conditionNode;
    @Child private PyStatementNode thenNode;
    @Child private PyStatementNode elseNode;

    private final ConditionProfile conditionProfile = ConditionProfile.createCountingProfile();

    public PyIfNode(PyExpressionNode conditionNode,
                    PyStatementNode thenNode,
                    PyStatementNode elseNode) {
        this.conditionNode = conditionNode;
        this.thenNode = thenNode;
        this.elseNode = elseNode;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        if (conditionProfile.profile(evalCondition(frame))){
            thenNode.executeGeneric(frame);
        } else {
            if (elseNode != null) elseNode.executeGeneric(frame);
        }
        return null; // this cannot be assigned anywhere, but we need a return value
    }

    private boolean evalCondition(VirtualFrame frame){
        try {
            return conditionNode.executeBoolean(frame);
        } catch (UnexpectedResultException e) {
            return TypeCast.castToBoolean(e.getResult());
        }
    }

    @Override
    public void print(int indent) {
        printIndented("IF", indent);
        conditionNode.print(indent+1);
        thenNode.print(indent+1);
        if (elseNode!=null) elseNode.print(indent+1);
    }
}
