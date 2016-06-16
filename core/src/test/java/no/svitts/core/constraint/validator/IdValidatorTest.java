package no.svitts.core.constraint.validator;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static no.svitts.core.constraint.validator.MovieValidator.ID_MAX_LENGTH;
import static no.svitts.core.util.StringUtil.getRandomString;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IdValidatorTest {

    private IdValidator idValidator;
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Before
    public void setUp() {
        idValidator = new IdValidator();
        constraintValidatorContextMock = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilderMock);
        when(constraintViolationBuilderMock.addConstraintViolation()).thenReturn(constraintValidatorContextMock);
    }

    @Test
    public void isValid_IdTooLong_ShouldReturnFalse() {
        String id = getRandomString(ID_MAX_LENGTH + 1);
        assertFalse(idValidator.isValid(id, constraintValidatorContextMock));
    }

    @Test
    public void isValid_IdContainsIllegalCharacters_ShouldReturnFalse() {
        String[] illegalCharacters = {"\\~", "\\#", "\\@", "\\*", "\\+", "\\%", "\\<", "\\>", "\\[", "\\]", "\\|", "\"", "\\_", "\\^", "\\£", "\\$", "\\€", "\\´"};
        for (String illegalCharacter : illegalCharacters) {
            assertFalse(idValidator.isValid(illegalCharacter, constraintValidatorContextMock));
        }
    }
}
