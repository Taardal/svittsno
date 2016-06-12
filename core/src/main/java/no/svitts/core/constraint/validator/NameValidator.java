package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator extends CoreValidator implements ConstraintValidator<ValidName, String> {

    static final int NAME_MAX_LENGTH = 255;

    @Override
    public void initialize(ValidName validName) {

    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        boolean tooLong = string.length() > NAME_MAX_LENGTH;
        boolean containsIllegalCharacters = containsIllegalCharacters(string);
        if (tooLong) {
            addConstraintViolation("Name was too long.", constraintValidatorContext);
        }
        if (containsIllegalCharacters) {
            addConstraintViolation("Name contained illegal characters.", constraintValidatorContext);
        }
        return !tooLong && !containsIllegalCharacters;
    }

}
