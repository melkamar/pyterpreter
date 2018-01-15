package cz.melkamar.pyterpreter.exceptions;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 15.01.2018 21:15.
 */
public class UndefinedVariableException extends RuntimeException {
    private String variableName;

    public UndefinedVariableException(String variableName) {
        super("Variable '" + variableName + "' undefined");
        this.variableName = variableName;
    }
}
