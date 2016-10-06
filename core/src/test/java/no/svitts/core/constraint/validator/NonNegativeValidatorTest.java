package no.svitts.core.constraint.validator;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NonNegativeValidatorTest {

    private NonNegativeValidator nonNegativeValidator;
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Before
    public void setUp() {
        nonNegativeValidator = new NonNegativeValidator();
        constraintValidatorContextMock = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilderMock);
        when(constraintViolationBuilderMock.addConstraintViolation()).thenReturn(constraintValidatorContextMock);
    }

    @Test
    public void isValid_ValueIsANegativeNumber_ShouldReturnFalse() {
        assertFalse(nonNegativeValidator.isValid(-1, constraintValidatorContextMock));
    }

    @Test
    public void isValid_ValueIsZero_ShouldReturnTrue() {
        assertTrue(nonNegativeValidator.isValid(0, constraintValidatorContextMock));
    }

    @Test
    public void isValid_ValueIsAboveZero_ShouldReturnTrue() {
        assertTrue(nonNegativeValidator.isValid(1, constraintValidatorContextMock));
    }

}
