package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.validator.LimitValidator;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LimitValidatorTest {

    private LimitValidator limitValidator;
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Before
    public void setUp() {
        limitValidator = new LimitValidator();
        constraintValidatorContextMock = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilderMock);
        when(constraintViolationBuilderMock.addConstraintViolation()).thenReturn(constraintValidatorContextMock);
    }

    @Test
    public void isValid_LimitIsANegativeNumber_ShouldReturnFalse() {
        assertFalse(limitValidator.isValid(-1, constraintValidatorContextMock));
    }
    
}
