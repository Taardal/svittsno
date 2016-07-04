package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.Length;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LengthValidatorTest {

    private LengthValidator lengthValidator;
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Before
    public void setUp() {
        lengthValidator = new LengthValidator();
        constraintValidatorContextMock = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilderMock);
        when(constraintViolationBuilderMock.addConstraintViolation()).thenReturn(constraintValidatorContextMock);
    }

    @Test
    public void isValid_LengthShorterThanMaxLength_ShouldReturnTrue() {
        Length lengthMock = mock(Length.class);
        when(lengthMock.length()).thenReturn(2);
        lengthValidator.initialize(lengthMock);
        assertTrue(lengthValidator.isValid("a", constraintValidatorContextMock));
    }

    @Test
    public void isValid_LengthEqualToMaxLength_ShouldReturnTrue() {
        Length lengthMock = mock(Length.class);
        when(lengthMock.length()).thenReturn(1);
        lengthValidator.initialize(lengthMock);
        assertTrue(lengthValidator.isValid("a", constraintValidatorContextMock));
    }

    @Test
    public void isValid_LengthGreaterThanMaxLength_ShouldReturnFalse() {
        Length lengthMock = mock(Length.class);
        when(lengthMock.length()).thenReturn(1);
        lengthValidator.initialize(lengthMock);
        assertFalse(lengthValidator.isValid("abc", constraintValidatorContextMock));
    }

}
