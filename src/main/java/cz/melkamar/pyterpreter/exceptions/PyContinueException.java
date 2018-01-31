package cz.melkamar.pyterpreter.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class PyContinueException extends ControlFlowException {
    public static final PyContinueException SINGLETON = new PyContinueException();

    private PyContinueException() {
    }
}
