package no.svitts.core.constraint.validator;

import no.svitts.core.date.ReleaseDate;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateValidatorTest {

    private DateValidator dateValidator;
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Before
    public void setUp() {
        dateValidator = new DateValidator();
        constraintValidatorContextMock = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilderMock);
        when(constraintViolationBuilderMock.addConstraintViolation()).thenReturn(constraintValidatorContextMock);
    }

    @Test
    public void isValid_ValidDate_ShouldReturnTrue() {
        ReleaseDate releaseDate = new ReleaseDate(2016, 1, 1);
        assertTrue(dateValidator.isValid(releaseDate.getTime(), constraintValidatorContextMock));
    }

    @Test
    public void isValid_DateBeforeTheFirstOfJanuaryNineteenHundred_ShouldReturnFalse() {
        ReleaseDate releaseDate = new ReleaseDate(1899, 1, 1);
        assertFalse(dateValidator.isValid(releaseDate.getTime(), constraintValidatorContextMock));
    }

}
