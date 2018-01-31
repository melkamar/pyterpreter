package cz.melkamar.pyterpreter;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.MaterializedFrame;
import cz.melkamar.pyterpreter.functions.PyBuiltinFunction;
import cz.melkamar.pyterpreter.nodes.builtin.PyInputBuiltinNode;
import cz.melkamar.pyterpreter.nodes.builtin.PyPrintBuiltinNode;
import cz.melkamar.pyterpreter.nodes.builtin.PySleepBuiltinNode;
import cz.melkamar.pyterpreter.nodes.builtin.PyTimeBuiltinNode;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 05.11.2017 22:13.
 */
public class Environment {
    public static boolean DEBUG_MODE = false;
    private final FrameDescriptor baseFrameDescriptor;
    private final MaterializedFrame baseFrame;
    private final InputStream stdin;
    private final PrintStream stdout;
    private final PrintStream stderr;

//    public static Environment DEFAULT = new EnvironmentBuilder().createEnvironment();

    protected Environment(FrameDescriptor baseFrameDescriptor,
                          MaterializedFrame baseFrame,
                          InputStream stdin,
                          PrintStream stdout, PrintStream stderr) {
        this.baseFrameDescriptor = baseFrameDescriptor;
        this.baseFrame = baseFrame;
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;

        initializeBuiltins();
    }

    private void initializeBuiltins() {
        PyBuiltinFunction[] builtinFunctions = {
                new PyBuiltinFunction("print", new PyPrintBuiltinNode(stdout)),
                new PyBuiltinFunction("input", new PyInputBuiltinNode(stdin)),
                new PyBuiltinFunction("time", new PyTimeBuiltinNode()),
                new PyBuiltinFunction("sleep", new PySleepBuiltinNode()),
        };

        for (PyBuiltinFunction builtinFunction: builtinFunctions){
            baseFrame.setObject(baseFrameDescriptor.findOrAddFrameSlot(builtinFunction.getName()), builtinFunction);
        }

        if (DEBUG_MODE) {
            System.out.println("Initialized builtins for frame " + baseFrame + " with descriptor " + baseFrameDescriptor);
        }
    }


    public FrameDescriptor getBaseFrameDescriptor() {
        return baseFrameDescriptor;
    }

    public MaterializedFrame getBaseFrame() {
        return baseFrame;
    }

    public static MaterializedFrame getDefaultFrame(FrameDescriptor frameDescriptor) {
        MaterializedFrame frame = Truffle.getRuntime().createMaterializedFrame(null, frameDescriptor);
        frame.setLong(frameDescriptor.findOrAddFrameSlot("ROOT", FrameSlotKind.Long), 42);
        return frame;
    }

    public InputStream getStdin() {
        return stdin;
    }

    public PrintStream getStdout() {
        return stdout;
    }

    public PrintStream getStderr() {
        return stderr;
    }
}
