package cz.melkamar.pyterpreter.nodes.function;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.functions.PyFunction;
import cz.melkamar.pyterpreter.functions.PyUserFunction;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.nodes.PyStatementNode;
import cz.melkamar.pyterpreter.nodes.PySuiteNode;

import java.util.Arrays;

/**
 * Add a new function to environment.
 */
public class PyDefFuncNode extends PyStatementNode {
    private String name;
    private String[] args;
    private FrameDescriptor frameDescriptor;
    @Child
    private PySuiteNode suiteNode;

    public PyDefFuncNode(String name, String[] args, PySuiteNode suiteNode, FrameDescriptor frameDescriptor) {
        this.name = name;
        this.args = args;
        this.suiteNode = suiteNode;
        this.frameDescriptor = frameDescriptor;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        PyRootNode rootNode = new PyRootNode(suiteNode, frameDescriptor);
        RootCallTarget target = Truffle.getRuntime().createCallTarget(rootNode);
        PyFunction userFunction = new PyUserFunction(name, target);

        FrameSlot slot = frame.getFrameDescriptor().findOrAddFrameSlot(name);
        frame.setObject(slot, userFunction);

        return null; // result of def cannot be assigned anywhere anyway
    }

    @Override
    public void print(int indent) {
        printIndented("def " + name + " " + Arrays.toString(args) + " (fd " + frameDescriptor + ")", indent);
        suiteNode.print(indent + 1);
    }
}
