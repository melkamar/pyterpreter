package cz.melkamar.pyterpreter.exceptions;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 15.01.2018 21:10.
 */
public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
