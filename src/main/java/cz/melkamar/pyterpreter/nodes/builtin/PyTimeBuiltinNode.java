package cz.melkamar.pyterpreter.nodes.builtin;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;

public class PyTimeBuiltinNode extends PyBuiltinNode {
    public PyTimeBuiltinNode() {
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return getTime();
    }

    @CompilerDirectives.TruffleBoundary
    private Long getTime(){
        return System.nanoTime();
    }

    @Override
    public void print(int indent) {
        printIndented("TIME", indent);
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
