package no.svitts.core.exception;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

public class AlreadyExitstsException extends ClientErrorException {

    public AlreadyExitstsException(String message) {
        super(message, Response.Status.CONFLICT);
    }

    public AlreadyExitstsException(String message, Throwable cause) {
        super(message, Response.Status.CONFLICT, cause);
    }

}
