package cz.melkamar.pyterpreter.nodes.expr;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import cz.melkamar.pyterpreter.TypeCast;
import cz.melkamar.pyterpreter.nodes.PyExpressionNode;

@NodeChild("valueNode")
public abstract class PyNotNode extends PyExpressionNode{

    @Specialization
    protected boolean negateBoolean(boolean value){
        return !value;
    }

    @Specialization
    protected boolean negateGeneric(Object value){
        return !TypeCast.castToBoolean(value);
    }

    protected abstract PyExpressionNode getValueNode();

    @Override
    public void print(int indent) {
        printIndented("NOT", indent);
        getValueNode().print(indent+1);
    }
}
