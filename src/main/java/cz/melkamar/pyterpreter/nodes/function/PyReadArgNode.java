package cz.melkamar.pyterpreter.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.exceptions.WrongParameterCountException;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;

public class PyReadArgNode extends PyExpressionNode {
    private final int index;

    public PyReadArgNode(int index) {
        this.index = index;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = frame.getArguments();
        if (index+1 >= args.length) throw new WrongParameterCountException("Wrong parameter count " + (args.length-1));
        return args[index+1];
    }

    @Override
    public void print(int indent) {
        printIndented("ReadARG " + index, indent);
    }
}
