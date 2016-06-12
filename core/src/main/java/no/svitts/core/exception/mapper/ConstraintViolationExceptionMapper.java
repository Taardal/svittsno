package no.svitts.core.exception.mapper;

import com.google.gson.Gson;
import no.svitts.core.error.ClientErrorMessage;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private Gson gson;

    public ConstraintViolationExceptionMapper() {
        gson = new Gson();
    }

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ClientErrorMessage clientErrorMessage = new ClientErrorMessage(Response.Status.BAD_REQUEST, exception.getMessage());
        return Response.status(clientErrorMessage.getStatus()).entity(gson.toJson(clientErrorMessage)).type(MediaType.APPLICATION_JSON).build();
    }

}
