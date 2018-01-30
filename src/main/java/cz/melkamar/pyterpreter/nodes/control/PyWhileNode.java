package cz.melkamar.pyterpreter.nodes.control;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.LoopNode;
import com.oracle.truffle.api.profiles.ConditionProfile;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;
import cz.melkamar.pyterpreter.nodes.PyStatementNode;
import cz.melkamar.pyterpreter.truffle.PyNoneType;

public final class PyWhileNode extends PyStatementNode {
//    @Child private PyExpressionNode conditionNode;
//    @Child private PyStatementNode bodyNode;
    @Child private LoopNode loopNode;
    private PyWhileRepeatingNode repeatingNode;

    private final ConditionProfile conditionProfile = ConditionProfile.createCountingProfile();

    public PyWhileNode(PyExpressionNode conditionNode,
                       PyStatementNode bodyNode) {
        this.repeatingNode = new PyWhileRepeatingNode(conditionNode, bodyNode);
        this.loopNode = Truffle.getRuntime().createLoopNode(repeatingNode);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        loopNode.executeLoop(frame);
        return PyNoneType.NONE_SINGLETON;
    }

    @Override
    public void print(int indent) {
        repeatingNode.print(indent);
    }
}
