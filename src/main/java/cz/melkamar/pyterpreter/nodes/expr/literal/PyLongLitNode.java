package cz.melkamar.pyterpreter.nodes.expr.literal;

import com.oracle.truffle.api.frame.VirtualFrame;

public class PyLongLitNode extends PyLiteralNode {
    private final long value;

    public PyLongLitNode(long value) {
        this.value = value;
    }

    public PyLongLitNode(String value) {
        this.value = Long.parseLong(value);
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

    @Override
    public void print(int indent) {
        printIndented("["+value+"]", indent+1);
    }
}
