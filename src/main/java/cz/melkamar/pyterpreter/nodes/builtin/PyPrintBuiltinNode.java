package cz.melkamar.pyterpreter.nodes.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.nodes.function.PyReadArgNode;
import cz.melkamar.pyterpreter.types.PyNoneType;

import java.io.PrintStream;

public final class PyPrintBuiltinNode extends PyBuiltinNode {
    @Child private PyReadArgNode textArg;
    private final PrintStream stdout;

    public PyPrintBuiltinNode(PrintStream stdout) {
        this.stdout = stdout;
        textArg = new PyReadArgNode(0);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object textToPrint = textArg.executeGeneric(frame);
        stdout.println(textToPrint);
        return PyNoneType.NONE_SINGLETON;
    }

    @Override
    public void print(int indent) {
        printIndented("PRINT", indent);
    }
}
