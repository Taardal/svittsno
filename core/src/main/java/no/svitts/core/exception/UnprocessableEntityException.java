package no.svitts.core.exception;

import javax.ws.rs.ClientErrorException;

public class UnprocessableEntityException extends ClientErrorException {

    private static final int STATUS_CODE = 422;

    public UnprocessableEntityException(String message) {
        super(message, STATUS_CODE);
    }

    public UnprocessableEntityException(String message, Throwable cause) {
        super(message, STATUS_CODE, cause);
    }

}
