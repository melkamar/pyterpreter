package cz.melkamar.pyterpreter.nodes.typed;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyNode;

public class FloatingNumberNode extends PyNode {
    public final double number;

    public FloatingNumberNode(Double number) {
        this.number = number;
    }

    public FloatingNumberNode(String string) {
        this.number = Double.parseDouble(string);
    }

    @Override
    public Object execute(Environment env) {
        return this.number;
    }

    @Override
    public String toString() {
        return "" + number;
    }
}
