package cz.melkamar.pyterpreter.nodes.typed;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyNode;

public class StringNode extends PyNode {
    public final String text;

    public StringNode(String text) {
        // Parser provides string including parentheses, so remove first and last char
        String txt = text;
        this.text = txt.substring(1, txt.length() - 1);
    }

    @Override
    public Object execute(Environment env) {
        return this.text;
    }

    @Override
    public void print(int indent) {
        printIndented(text, indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }
}
