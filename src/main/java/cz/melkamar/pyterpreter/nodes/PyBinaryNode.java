package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;

@NodeChildren({@NodeChild("leftChild"), @NodeChild("rightChild")})
public abstract class PyBinaryNode extends PyExpressionNode {
}
