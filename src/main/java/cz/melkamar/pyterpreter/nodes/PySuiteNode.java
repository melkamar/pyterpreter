package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "suite")
public final class PySuiteNode extends PyStatementNode {
    @Children
    private final PyStatementNode[] statements;

    /**
     * If true, result of last child statement will be returned to the parent node (even without return, for REPL).
     */
    private final boolean returnLastResult;

    public PySuiteNode(PyStatementNode[] statements, boolean returnLastResult) {
        this.statements = statements;
        this.returnLastResult = returnLastResult;
    }

    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame) {
        // keep track of result for REPL
        Object lastResult = null;
        for (PyStatementNode statement : statements) {
            lastResult = statement.executeGeneric(frame);
        }
        return lastResult;
    }

    @Override
    public void print(int indent) {
        printIndented("suite", indent+1);
    }
}
