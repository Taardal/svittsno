package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.NonNegative;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NonNegativeValidator extends CoreValidator implements ConstraintValidator<NonNegative, Number> {

    @Override
    public void initialize(NonNegative constraintAnnotation) {

    }

    @Override
    public boolean isValid(Number number, ConstraintValidatorContext constraintValidatorContext) {
        if (isNonNegative(number)) {
            return true;
        } else {
            addConstraintViolation("Number is below 0 (zero). Must be equal to or greater than 0 (zero).", constraintValidatorContext);
            return false;
        }
    }

    private boolean isNonNegative(Number number) {
        if (number instanceof Long) {
            return (Long) number >= 0;
        } else if (number instanceof Double) {
            return (Double) number >= 0;
        } else if (number instanceof Float) {
            return (Float) number >= 0;
        } else {
            return (Integer) number >= 0;
        }
    }

}
