package no.svitts.core.exception;

public class NoChangeException extends Exception {

    public NoChangeException(String message) {
        super(message);
    }

    public NoChangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
