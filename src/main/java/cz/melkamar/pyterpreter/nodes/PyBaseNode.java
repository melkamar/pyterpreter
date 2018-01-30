package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.nodes.Node;

public abstract class PyBaseNode extends Node {
    public abstract void print(int indent);

    protected void printIndented(String text, int indent) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            builder.append("  ");
        }
        builder.append(text);
        System.out.println(builder.toString());
    }
}
