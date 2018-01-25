package cz.melkamar.pyterpreter.nodes.typed;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
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
    public Object execute(VirtualFrame frame) {
        return this.number;
    }

    @Override
    public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
        return this.number;
    }

    @Override
    public String toString() {
        return "" + number;
    }
}
