package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidCharacters;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterValidator extends CoreValidator implements ConstraintValidator<ValidCharacters, String> {

    @Override
    public void initialize(ValidCharacters validCharacters) {
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (isValidCharacters(string)) {
            return true;
        } else {
            addConstraintViolation("Contains illegal characters.", constraintValidatorContext);
            return false;
        }
    }

    private boolean isValidCharacters(String string) {
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"_^£\\$€´]");
        Matcher matcher = pattern.matcher(string);
        return !matcher.find();
    }

}
