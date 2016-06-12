package no.svitts.core.exception.mapper;

import no.svitts.core.error.ClientErrorMessage;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebApplicationExceptionMapperTest {

    private WebApplicationExceptionMapper webApplicationExceptionMapper;

    @Before
    public void setUp() {
        webApplicationExceptionMapper = new WebApplicationExceptionMapper();
    }

    @Test
    public void toResponse_ShouldReturnResponseWithClientErrorResponseEntityWithCorrectRequestStatusAndExceptionMessage() throws Exception {
        String message = "An web application exception has occurred.";
        int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        WebApplicationException webApplicationExceptionMock = mock(WebApplicationException.class);
        Response responseMock = mock(Response.class);

        when(webApplicationExceptionMock.getMessage()).thenReturn(message);
        when(webApplicationExceptionMock.getResponse()).thenReturn(responseMock);
        when(responseMock.getStatus()).thenReturn(statusCode);

        Response response = webApplicationExceptionMapper.toResponse(webApplicationExceptionMock);
        ClientErrorMessage clientErrorMessage = (ClientErrorMessage) response.getEntity();

        assertEquals(500, response.getStatus());
        assertEquals(1, clientErrorMessage.getMessages().size());
        assertEquals(message, clientErrorMessage.getMessages().get(0));
    }
}
