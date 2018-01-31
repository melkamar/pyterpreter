package cz.melkamar.pyterpreter.nodes.expr.literal;

import com.oracle.truffle.api.frame.VirtualFrame;

public class PyBooleanLitNode extends PyLiteralNode {
    private boolean value;
    public PyBooleanLitNode(boolean value) {
        this.value = value;
    }

    // TODO tady throws UnexpectedResultException? možná to není potřeba
    @Override
    public boolean executeBoolean(VirtualFrame frame)  {
        return value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return value;
    }

    @Override
    public void print(int indent) {
        printIndented("["+value+"]", indent+1);
    }
}
