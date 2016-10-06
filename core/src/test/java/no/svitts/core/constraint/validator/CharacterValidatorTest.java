package no.svitts.core.constraint.validator;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CharacterValidatorTest {

    private CharacterValidator characterValidator;
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Before
    public void setUp() {
        characterValidator = new CharacterValidator();
        constraintValidatorContextMock = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilderMock);
        when(constraintViolationBuilderMock.addConstraintViolation()).thenReturn(constraintValidatorContextMock);
    }

    @Test
    public void isValid_ValidCharacters_ShouldReturnTrue() {
        assertTrue(characterValidator.isValid("a", constraintValidatorContextMock));
    }

    @Test
    public void isValid_InvalidCharacters_ShouldReturnFalse() {
        String[] illegalCharacters = {"\\~", "\\#", "\\@", "\\*", "\\+", "\\%", "\\<", "\\>", "\\[", "\\]", "\\|", "\\^", "\\£", "\\$", "\\€", "\\´"};
        for (String illegalCharacter : illegalCharacters) {
            assertFalse(characterValidator.isValid(illegalCharacter, constraintValidatorContextMock));
        }
    }
}
