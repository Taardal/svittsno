package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.Length;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LengthValidator extends CoreValidator implements ConstraintValidator<Length, String> {

    private int length;

    @Override
    public void initialize(Length length) {
        this.length = length.length();
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null || string.length() <= length) {
            return true;
        } else {
            addConstraintViolation("Length of string is too long [" + string + "]", constraintValidatorContext);
            return false;
        }
    }

}
