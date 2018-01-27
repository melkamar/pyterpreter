package cz.melkamar.pyterpreter.nodes.expr.literal;

import com.oracle.truffle.api.frame.VirtualFrame;

import java.math.BigInteger;

public class PyBigNumLitNode extends PyLiteralNode {
    private final BigInteger value;

    public PyBigNumLitNode(BigInteger value) {
        this.value = value;
    }

    public PyBigNumLitNode(String value) {
        this.value = new BigInteger(value);
    }

    // TODO tady throws UnexpectedResultException? možná to není potřeba

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return value;
    }

    @Override
    public void print(int indent) {
        printIndented("["+value+"]", indent+1);
    }
}
