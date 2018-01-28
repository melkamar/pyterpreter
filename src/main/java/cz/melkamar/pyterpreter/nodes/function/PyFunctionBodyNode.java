package cz.melkamar.pyterpreter.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;
import cz.melkamar.pyterpreter.exceptions.ReturnException;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;
import cz.melkamar.pyterpreter.nodes.PySuiteNode;

public final class PyFunctionBodyNode extends PyExpressionNode{
    @Child private PySuiteNode body;

    private final BranchProfile exceptionTaken = BranchProfile.create();
    private final BranchProfile nullTaken = BranchProfile.create();

    public PyFunctionBodyNode(PySuiteNode body) {
        this.body = body;
        // TODO addRootTag() ?
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        try {
            /* Execute the function body. */
            body.executeGeneric(frame);

        } catch (ReturnException ex) {
            exceptionTaken.enter();
            return ex.getResult();
        }

        nullTaken.enter();
        return null; // TODO create NoneType?
    }

    @Override
    public void print(int indent) {
        printIndented("BODY", indent);
        body.print(indent);
    }
}
