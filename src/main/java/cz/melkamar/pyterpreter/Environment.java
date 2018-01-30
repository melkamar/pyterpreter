package cz.melkamar.pyterpreter;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 05.11.2017 22:13.
 */
public class Environment {
    public static boolean DEBUG_MODE = false;
    private final FrameDescriptor defaultFrameDescriptor;
    private final MaterializedFrame defaultFrame;

    public static Environment DEFAULT = new Environment();

    public Environment() {
        this.defaultFrameDescriptor = new FrameDescriptor();
        this.defaultFrame = initDefaultFrame(defaultFrameDescriptor);
    }

    public FrameDescriptor getDefaultFrameDescriptor() {
        return defaultFrameDescriptor;
    }

    public MaterializedFrame getDefaultFrame() {
        return defaultFrame;
    }

    private MaterializedFrame initDefaultFrame(FrameDescriptor frameDescriptor) {
        Object[] defaultArgs = {};
        return Truffle.getRuntime().createMaterializedFrame(defaultArgs, frameDescriptor);
    }
}
