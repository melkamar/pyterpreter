package cz.melkamar.pyterpreter.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;
import cz.melkamar.pyterpreter.exceptions.ReturnException;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;
import cz.melkamar.pyterpreter.nodes.PySuiteNode;
import cz.melkamar.pyterpreter.types.PyNoneType;

public final class PyFunctionRootNode extends PyExpressionNode {
    @Child private PySuiteNode body;

    private final BranchProfile exceptionTaken = BranchProfile.create();
    private final BranchProfile nullTaken = BranchProfile.create();

    public PyFunctionRootNode(PySuiteNode body) {
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
        return PyNoneType.NONE_SINGLETON;
    }

    @Override
    public void print(int indent) {
        printIndented("BODY", indent);
        body.print(indent);
    }
}
