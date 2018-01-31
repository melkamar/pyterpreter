package cz.melkamar.pyterpreter.nodes.builtin;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import cz.melkamar.pyterpreter.nodes.function.PyReadArgNode;
import cz.melkamar.pyterpreter.types.PyNoneType;

import java.io.PrintStream;

@NodeChild(value = "argNode", type = PyReadArgNode.class)
@NodeField(name = "stdout", type = PrintStream.class)
public abstract class PyPrintBuiltinNode extends PyBuiltinNode {
    protected abstract PrintStream getStdout();

    @Specialization
    public Object printString(String toPrint) {
        getStdout().println(toPrint);
        return PyNoneType.NONE_SINGLETON;
    }

    @Specialization
    public Object printLong(long toPrint) {
        getStdout().println(String.valueOf(toPrint));
        return PyNoneType.NONE_SINGLETON;
    }

    @Specialization
    public Object printBool(boolean toPrint) {
        if (toPrint) getStdout().println("True");
        else getStdout().println("False");
        return PyNoneType.NONE_SINGLETON;
    }

    @Specialization
    public Object printGeneric(Object toPrint) {
        getStdout().println(toPrint.toString());
        return PyNoneType.NONE_SINGLETON;
    }


    @Override
    public void print(int indent) {
        printIndented("PRINT", indent);
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
