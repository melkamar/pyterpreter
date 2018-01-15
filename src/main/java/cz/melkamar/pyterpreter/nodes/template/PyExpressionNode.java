package cz.melkamar.pyterpreter.nodes.template;

import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 03.11.2017 10:19.
 */
public abstract class PyExpressionNode extends PyStatementNode {
    public abstract Object execute(VirtualFrame frame);

    // TODO run this on GraalVM?
//    public long executeLong(VirtualFrame virtualFrame){
//        return PyTypesGen.expectLong(execute(virtualFrame));
//    }
}
