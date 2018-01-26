package cz.melkamar.pyterpreter.nodes.expr.literal;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;

public class PyLongLitNode extends PyExpressionNode {
    private final long value;

    public PyLongLitNode(long value) {
        this.value = value;
    }

    // TODO tady throws UnexpectedResultException? možná to není potřeba
    @Override
    public long executeLong(VirtualFrame frame)  {
        return value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return value;
    }
}
