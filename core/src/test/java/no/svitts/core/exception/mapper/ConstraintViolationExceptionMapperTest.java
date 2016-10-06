package no.svitts.core.exception.mapper;

import no.svitts.core.errormessage.ClientErrorMessage;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConstraintViolationExceptionMapperTest {

    private ConstraintViolationExceptionMapper constraintViolationExceptionMapper;

    @Before
    public void setUp() {
        constraintViolationExceptionMapper = new ConstraintViolationExceptionMapper();
    }

    @Test
    public void toResponse_ShouldReturnResponseWithClientErrorResponseEntityWithBadRequestStatusAndConstraintViolationsMessages() {
        String constraintViolationMessage = "An constraint validation exception occurred.";

        ConstraintViolationException constraintViolationExceptionMock = mock(ConstraintViolationException.class);
        ConstraintViolation constraintViolationMock = mock(ConstraintViolation.class);

        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        constraintViolations.add(constraintViolationMock);

        when(constraintViolationExceptionMock.getConstraintViolations()).thenReturn(constraintViolations);
        when(constraintViolationMock.getMessage()).thenReturn(constraintViolationMessage);

        Response response = constraintViolationExceptionMapper.toResponse(constraintViolationExceptionMock);
        ClientErrorMessage clientErrorMessage = (ClientErrorMessage) response.getEntity();

        assertEquals(400, response.getStatus());
        assertEquals(1, clientErrorMessage.getMessages().size());
        assertEquals(constraintViolationMessage, clientErrorMessage.getMessages().get(0));
        response.close();
    }
}
