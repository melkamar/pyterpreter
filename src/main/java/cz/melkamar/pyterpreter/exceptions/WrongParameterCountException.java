package cz.melkamar.pyterpreter.exceptions;

public class WrongParameterCountException extends RuntimeException {
    public WrongParameterCountException(String message) {
        super(message);
    }
}
