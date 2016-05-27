package no.svitts.core.exception;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

public class ConflictException extends ClientErrorException {

    public ConflictException(String message) {
        super(message, Response.Status.CONFLICT);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, Response.Status.CONFLICT, cause);
    }

}
