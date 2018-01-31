package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.melkamar.pyterpreter.truffle.PyTypes;
import cz.melkamar.pyterpreter.truffle.PyTypesGen;

@TypeSystemReference(PyTypes.class)
@NodeInfo(description = "Abstract expression")
public abstract class PyExpressionNode extends PyStatementNode {
    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return PyTypesGen.expectLong(executeGeneric(frame));
    }

    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return PyTypesGen.expectBoolean(executeGeneric(frame));
    }
}
