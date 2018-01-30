package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import cz.melkamar.pyterpreter.Environment;

public class PyRootNode extends RootNode {
    private final PyStatementNode child;
    public VirtualFrame lastExecutionFrame = null;

    public PyRootNode(PyStatementNode child, FrameDescriptor frameDescriptor) {
        super(null, frameDescriptor);
        this.child = child;
    }

    public Object getFrameValue(String key){
        FrameSlot slot = lastExecutionFrame.getFrameDescriptor().findFrameSlot(key);
        if (slot == null) return null;
        return lastExecutionFrame.getValue(slot);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        lastExecutionFrame = frame; // Keep track of last frame for debugging
        // keep track of result for REPL
        return child.executeGeneric(frame);
    }

    public void print(){
        System.out.println("root (fd "+getFrameDescriptor()+")");
        child.print(1);
    }

    public Object run(){
        this.print();
        CallTarget target = Truffle.getRuntime().createCallTarget(this);
        Object result = target.call(Environment.DEFAULT.getDefaultFrame());
        return result;
    }
}
