package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.validator.GenreValidator;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static no.svitts.core.util.StringUtil.getRandomString;
import static no.svitts.core.constraint.validator.GenreValidator.GENRE_MAX_LENGTH;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenreValidatorTest {

    private GenreValidator genreValidator;
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Before
    public void setUp() {
        genreValidator = new GenreValidator();
        constraintValidatorContextMock = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilderMock);
        when(constraintViolationBuilderMock.addConstraintViolation()).thenReturn(constraintValidatorContextMock);
    }

    @Test
    public void isValid_GenreTooLong_ShouldReturnFalse() {
        String id = getRandomString(GENRE_MAX_LENGTH + 1);
        assertFalse(genreValidator.isValid(id, constraintValidatorContextMock));
    }

    @Test
    public void isValid_GenreContainsIllegalCharacters_ShouldReturnFalse() {
        String[] strings = {"\\~", "\\#", "\\@", "\\*", "\\+", "\\%", "\\<", "\\>", "\\[", "\\]", "\\|", "\"", "\\_", "\\^", "\\£", "\\$", "\\€", "\\´"};
        for (String string : strings) {
            assertFalse(genreValidator.isValid(string, constraintValidatorContextMock));
        }
    }
    
}
