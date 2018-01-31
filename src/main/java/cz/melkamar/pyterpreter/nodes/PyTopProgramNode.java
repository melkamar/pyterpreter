package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import cz.melkamar.pyterpreter.Environment;

/**
 * The very top node of the AST.
 *
 * {@link PyRootNode} is a root node of any AST subtree, such as a function call.
 * This node is a specialization of {@link PyRootNode} and takes an environment in which to run. This environment
 * defines default values, builtins, I/O streams and such.
 */
public class PyTopProgramNode extends PyRootNode {
    private final Environment environment;

    public PyTopProgramNode(PyStatementNode child,
                            Environment environment) {
        super(child, environment.getBaseFrameDescriptor());
        this.environment = environment;
    }

    public Object run(){
        this.print();
        CallTarget target = Truffle.getRuntime().createCallTarget(this);

        // First argument in a call is a frame
        Object result = target.call(environment.getBaseFrame());
        return result;
    }
}
