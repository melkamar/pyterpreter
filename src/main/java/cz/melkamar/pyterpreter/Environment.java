package cz.melkamar.pyterpreter;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.MaterializedFrame;
import cz.melkamar.pyterpreter.functions.PyBuiltinFunction;
import cz.melkamar.pyterpreter.functions.builtin.PyPrintBuiltinFunction;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 05.11.2017 22:13.
 */
public class Environment {
    public static boolean DEBUG_MODE = true;
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

        // TODO initialize frame with builtin methods. print and input will have stdin/stdout as their streams
        initializeBuiltins();
    }

    private void initializeBuiltins(){
        PyBuiltinFunction builtinFunction = new PyPrintBuiltinFunction(this);
        baseFrame.setObject(baseFrameDescriptor.findOrAddFrameSlot(builtinFunction.getName()), builtinFunction);

        if (DEBUG_MODE){
            System.out.println("Initialized builtins for frame "+baseFrame + " with descriptor "+baseFrameDescriptor);
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
