package no.svitts.core.exception.mapper;

import no.svitts.core.errormessage.ClientErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        ClientErrorMessage clientErrorMessage = new ClientErrorMessage(exception.getResponse().getStatus(), exception.getMessage());
        return Response.status(clientErrorMessage.getStatus()).entity(clientErrorMessage).type(MediaType.APPLICATION_JSON).build();
    }

}
