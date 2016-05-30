package no.svitts.core.exception.mapper;

import com.google.gson.Gson;
import no.svitts.core.error.ErrorMessage;

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
    public Response toResponse(WebApplicationException webApplicationException) {
        ErrorMessage errorMessage = new ErrorMessage(webApplicationException);
        return Response.status(errorMessage.getStatus()).entity(gson.toJson(errorMessage)).type(MediaType.APPLICATION_JSON).build();
    }

}
