package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdValidator extends CoreValidator implements ConstraintValidator<ValidId, String> {

    static final int ID_MAX_LENGTH = 255;

    @Override
    public void initialize(ValidId validId) {

    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        boolean tooLong = string.length() > ID_MAX_LENGTH;
        boolean containsIllegalCharacters = containsIllegalCharacters(string);
        if (tooLong) {
            addConstraintViolation("ID was too long.", constraintValidatorContext);
        }
        if (containsIllegalCharacters) {
            addConstraintViolation("ID contained illegal characters.", constraintValidatorContext);
        }
        return !tooLong && !containsIllegalCharacters;
    }

}
