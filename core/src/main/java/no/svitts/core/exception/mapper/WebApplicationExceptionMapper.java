package no.svitts.core.exception.mapper;

import com.google.gson.Gson;
import no.svitts.core.error.ClientErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    private Gson gson;

    public WebApplicationExceptionMapper() {
        gson = new Gson();
    }

    @Override
    public Response toResponse(WebApplicationException exception) {
        ClientErrorMessage clientErrorMessage = new ClientErrorMessage(exception.getResponse().getStatus(), exception.getMessage());
        return Response.status(clientErrorMessage.getStatus()).entity(gson.toJson(clientErrorMessage)).type(MediaType.APPLICATION_JSON).build();
    }

}
