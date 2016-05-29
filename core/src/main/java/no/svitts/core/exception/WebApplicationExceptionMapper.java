package no.svitts.core.exception;

import com.google.gson.Gson;

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
        HttpErrorMessage httpErrorMessage = new HttpErrorMessage(webApplicationException);
        return Response.status(httpErrorMessage.getStatus()).entity(gson.toJson(httpErrorMessage)).type(MediaType.APPLICATION_JSON).build();
    }

}
