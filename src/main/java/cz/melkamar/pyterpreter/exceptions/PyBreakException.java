package cz.melkamar.pyterpreter.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class PyBreakException extends ControlFlowException {
    public static final PyBreakException SINGLETON = new PyBreakException();

    private PyBreakException() {
    }
}
