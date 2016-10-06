package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.NotNullOrEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullOrEmptyValidator extends CoreValidator implements ConstraintValidator<NotNullOrEmpty, String> {

    @Override
    public void initialize(NotNullOrEmpty notNullOrEmpty) {

    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string != null && !string.equals("")) {
            return true;
        } else {
            addConstraintViolation("String is null or empty.", constraintValidatorContext);
            return false;
        }
    }

}
