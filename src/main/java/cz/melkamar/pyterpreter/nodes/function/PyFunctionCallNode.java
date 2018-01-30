package cz.melkamar.pyterpreter.nodes.function;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import cz.melkamar.pyterpreter.exceptions.ReturnException;
import cz.melkamar.pyterpreter.exceptions.UndefinedVariableException;
import cz.melkamar.pyterpreter.functions.PyFunction;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;
import cz.melkamar.pyterpreter.types.PyNoneType;

public class PyFunctionCallNode extends PyExpressionNode {
    private String name;
    @Children
    private final PyExpressionNode[] argNodes;

    public PyFunctionCallNode(String name, PyExpressionNode[] argNodes) {
        this.name = name;
        if (argNodes == null) this.argNodes = new PyExpressionNode[0];
        else this.argNodes = argNodes;
    }

    @ExplodeLoop
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = new Object[argNodes.length];
        for (int i = 0; i < argNodes.length; i++) args[i] = argNodes[i].executeGeneric(frame);

        FrameSlot slot = frame.getFrameDescriptor().findFrameSlot(name);
        try {
            PyFunction function = (PyFunction) frame.getObject(slot);
            function.getCallTarget().call(args);
            return PyNoneType.NONE_SINGLETON;
        } catch (FrameSlotTypeException | NullPointerException e) {
            throw new UndefinedVariableException(name);
        } catch (ReturnException e){
            return e.getResult();
        }
    }

    @Override
    public void print(int indent) {
        printIndented("CALL <" + name + ">", indent);
        for (PyExpressionNode arg : argNodes) arg.print(indent + 1);
    }
}
