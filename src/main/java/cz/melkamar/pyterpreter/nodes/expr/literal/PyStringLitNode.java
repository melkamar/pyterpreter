package cz.melkamar.pyterpreter.nodes.expr.literal;

import com.oracle.truffle.api.frame.VirtualFrame;

public class PyStringLitNode extends PyLiteralNode {
    private final String text;

    public PyStringLitNode(String text) {
        this.text = text.substring(1, text.length() - 1);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return text;
    }

    @Override
    public void print(int indent) {
        printIndented("[" + text + "]", indent + 1);
    }
}
