package cz.melkamar.pyterpreter.nodes.builtin;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.io.InputStream;
import java.util.Scanner;

public final class PyInputBuiltinNode extends PyBuiltinNode {
    private final Scanner sc;

    public PyInputBuiltinNode(InputStream stdin) {
        sc = new Scanner(stdin);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return readLine();
    }

    @CompilerDirectives.TruffleBoundary
    private String readLine(){
        return sc.nextLine();
    }

    @Override
    public void print(int indent) {
        printIndented("INPUT", indent);
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
