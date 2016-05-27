package no.svitts.core.exception;

public class RequiredFieldsInvalidException extends Exception {

    public RequiredFieldsInvalidException(String message) {
        super(message);
    }

    public RequiredFieldsInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
