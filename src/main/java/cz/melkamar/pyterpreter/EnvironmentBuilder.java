package cz.melkamar.pyterpreter;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;

import java.io.InputStream;
import java.io.PrintStream;

public class EnvironmentBuilder {
    private FrameDescriptor baseFrameDescriptor = new FrameDescriptor();
    private MaterializedFrame baseFrame;
    private InputStream stdin = System.in;
    private PrintStream stdout = System.out;
    private PrintStream stderr = System.err;

    public EnvironmentBuilder setBaseFrameDescriptor(FrameDescriptor baseFrameDescriptor) {
        this.baseFrameDescriptor = baseFrameDescriptor;
        return this;
    }

    public EnvironmentBuilder setBaseFrame(MaterializedFrame baseFrame) {
        this.baseFrame = baseFrame;
        return this;
    }

    public EnvironmentBuilder setStdin(InputStream stdin) {
        this.stdin = stdin;
        return this;
    }

    public EnvironmentBuilder setStdout(PrintStream stdout) {
        this.stdout = stdout;
        return this;
    }

    public EnvironmentBuilder setStderr(PrintStream stderr) {
        this.stderr = stderr;
        return this;
    }

    public Environment createEnvironment() {
        if (baseFrame == null) baseFrame = Environment.getDefaultFrame(baseFrameDescriptor);
        return new Environment(baseFrameDescriptor, baseFrame, stdin, stdout, stderr);
    }
}