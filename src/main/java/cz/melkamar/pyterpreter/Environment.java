package cz.melkamar.pyterpreter;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.MaterializedFrame;

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

        // TODO initialize frame with builtin methods. print and input will have stdin/stdout as their streams
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
}
