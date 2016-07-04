package no.svitts.core.constraint.validator;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotNullOrEmptyValidatorTest {

    private NotNullOrEmptyValidator notNullOrEmptyValidator;
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Before
    public void setUp() {
        notNullOrEmptyValidator = new NotNullOrEmptyValidator();
        constraintValidatorContextMock = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilderMock);
        when(constraintViolationBuilderMock.addConstraintViolation()).thenReturn(constraintValidatorContextMock);
    }

    @Test
    public void isValid_StringIsNotNullOrEmpty_ShouldReturnTrue() {
        assertTrue(notNullOrEmptyValidator.isValid("a", constraintValidatorContextMock));
    }

    @Test
    public void isValid_StringIsNull_ShouldReturnFalse() {
        assertFalse(notNullOrEmptyValidator.isValid(null, constraintValidatorContextMock));
    }

    @Test
    public void isValid_StringIsEmpty_ShouldReturnFalse() {
        assertFalse(notNullOrEmptyValidator.isValid("", constraintValidatorContextMock));
    }

}
