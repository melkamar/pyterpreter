package cz.melkamar.pyterpreter.nodes.typed;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyNode;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 05.11.2017 22:15.
 */
public class NumberNode extends PyNode {
    public final long number;

    public NumberNode(Long number) {
        this.number = number;
    }

    public NumberNode(String string){
        this.number = Long.parseLong(string);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return this.number;
    }

    @Override
    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return this.number;
    }

    @Override
    public void print(int indent) {
        printIndented(number+"", indent);
        for (PyNode child : children) {
            child.print(indent+1);
        }
    }
}
