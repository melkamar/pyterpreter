package cz.melkamar.pyterpreter.nodes.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.melkamar.pyterpreter.exceptions.TypeException;
import cz.melkamar.pyterpreter.nodes.function.PyReadArgNode;
import cz.melkamar.pyterpreter.types.PyNoneType;

public class PySleepBuiltinNode extends PyBuiltinNode {
    @Child private PyReadArgNode sleepArg;
    public PySleepBuiltinNode() {
        sleepArg = new PyReadArgNode(0);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        try {
            Thread.sleep(sleepArg.executeLong(frame) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnexpectedResultException e) {
            throw new TypeException("integer");
        }
        return PyNoneType.NONE_SINGLETON;
    }

    @Override
    public void print(int indent) {
        printIndented("TIME", indent);
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
