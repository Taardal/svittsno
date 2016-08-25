package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidCharacters;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterValidator extends CoreValidator implements ConstraintValidator<ValidCharacters, String> {

    private static final Pattern INVALID_CHARACTERS_PATTERN = Pattern.compile("[~#@*+%{}<>\\[\\]|\"_^£$€´]");

    @Override
    public void initialize(ValidCharacters validCharacters) {
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null || isValidCharacters(string)) {
            return true;
        } else {
            addConstraintViolation("Contains illegal characters.", constraintValidatorContext);
            return false;
        }
    }

    private boolean isValidCharacters(String string) {
        Matcher matcher = INVALID_CHARACTERS_PATTERN.matcher(string);
        return !matcher.find();
    }

}
