package cz.melkamar.pyterpreter.nodes.function;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.functions.PyFunction;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.nodes.PyStatementNode;

/**
 * Add a new function to environment.
 */
public class PyDefFuncNode extends PyStatementNode {
    public final PyFunction function;
    public final String name;

    public PyDefFuncNode(PyFunction function, String name) {
        this.function = function;
        this.name = name;
    }

    @Override
    public void print(int indent) {
        printIndented("def " + name, indent);
        ((PyRootNode) function.getCallTarget().getRootNode()).getChild().print(indent+1);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        FrameSlot slot = frame.getFrameDescriptor().findOrAddFrameSlot(name);
        frame.setObject(slot, function);
        function.setScope(frame.materialize());

        return null;
    }
}
