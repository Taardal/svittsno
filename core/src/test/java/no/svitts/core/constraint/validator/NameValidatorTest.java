package no.svitts.core.constraint.validator;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static no.svitts.core.constraint.validator.MovieValidator.NAME_MAX_LENGTH;
import static no.svitts.core.util.StringUtil.getRandomString;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NameValidatorTest {

    private NameValidator nameValidator;
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Before
    public void setUp() {
        nameValidator = new NameValidator();
        constraintValidatorContextMock = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilderMock);
        when(constraintViolationBuilderMock.addConstraintViolation()).thenReturn(constraintValidatorContextMock);
    }

    @Test
    public void isValid_NameTooLong_ShouldReturnFalse() {
        String id = getRandomString(NAME_MAX_LENGTH + 1);
        assertFalse(nameValidator.isValid(id, constraintValidatorContextMock));
    }

    @Test
    public void isValid_NameContainsIllegalCharacters_ShouldReturnFalse() {
        String[] illegalCharacters = {"\\~", "\\#", "\\@", "\\*", "\\+", "\\%", "\\<", "\\>", "\\[", "\\]", "\\|", "\"", "\\_", "\\^", "\\£", "\\$", "\\€", "\\´"};
        for (String illegalCharacter : illegalCharacters) {
            assertFalse(nameValidator.isValid(illegalCharacter, constraintValidatorContextMock));
        }
    }
    
}
