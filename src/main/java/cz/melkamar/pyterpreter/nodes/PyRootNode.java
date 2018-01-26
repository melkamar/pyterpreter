package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RootNode;

public class PyRootNode extends RootNode {
    private final PyStatementNode child;

    public PyRootNode(PyStatementNode child) {
        super(null);
        this.child = child;
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame frame) {
        // keep track of result for REPL
        return child.executeGeneric(frame);
    }

    public void print(){
        System.out.println("root");
        child.print(0);
    }
}
