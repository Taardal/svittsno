package no.svitts.core.exception.mapper;

import no.svitts.core.error.ClientErrorMessage;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> constraintViolationMessages = exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        ClientErrorMessage clientErrorMessage = new ClientErrorMessage(Response.Status.BAD_REQUEST, constraintViolationMessages);
        return Response.status(clientErrorMessage.getStatus()).entity(clientErrorMessage).type(MediaType.APPLICATION_JSON).build();
    }

}
