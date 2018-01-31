package cz.melkamar.pyterpreter.exceptions;

public class TypeException extends RuntimeException {
    public TypeException(String typename) {
        super(String.format("Type %s required.", typename));
    }
}
