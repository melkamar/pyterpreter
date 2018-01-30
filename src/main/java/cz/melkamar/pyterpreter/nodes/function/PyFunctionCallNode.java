package cz.melkamar.pyterpreter.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.exceptions.WrongParameterCountException;
import cz.melkamar.pyterpreter.functions.PyFunction;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;
import cz.melkamar.pyterpreter.nodes.PyReadVarNode;

public class PyFunctionCallNode extends PyExpressionNode {
    private PyReadVarNode nameNode;
    @Children
    private final PyExpressionNode[] argNodes;

    public PyFunctionCallNode(PyReadVarNode name, PyExpressionNode[] argNodes) {
        this.nameNode = name;
        if (argNodes == null) this.argNodes = new PyExpressionNode[0];
        else this.argNodes = argNodes;
    }

    @ExplodeLoop
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = new Object[argNodes.length + 1];
        args[0] = frame; // Pass current frame as first argument
        for (int i = 0; i < argNodes.length; i++) args[i + 1] = argNodes[i].executeGeneric(frame);

//        FrameSlot slot = frame.getFrameDescriptor().findFrameSlot(nameNode);
        try {
            Object result = nameNode.executeGeneric(frame);
            PyFunction function = (PyFunction) result;
            if (argNodes.length != function.argc)
                throw new WrongParameterCountException(String.format("Function %s expected %d arguments but %d given.",
                                                                     nameNode.getVarName(),
                                                                     function.argc,
                                                                     argNodes.length));
            return function.getCallTarget().call(args);
        } catch (ClassCastException e) {
            throw new NotImplementedException("Incompatible types");
        }
    }

    @Override
    public void print(int indent) {
        printIndented("CALL <" + nameNode.getVarName() + ">", indent);
        for (PyExpressionNode arg : argNodes) arg.print(indent + 1);
    }
}
