package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class PySuiteNode extends PyStatementNode {
    @Children
    private final PyStatementNode[] statements;

    public PySuiteNode(PyStatementNode[] statements) {
        this.statements = statements;
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
}
