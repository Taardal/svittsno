package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidLimit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LimitValidator extends CoreValidator implements ConstraintValidator<ValidLimit, Integer> {

    @Override
    public void initialize(ValidLimit constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        boolean negativeValue = value < 0;
        if (negativeValue) {
            addConstraintViolation("Limit was a negative number. Limit must be equal to or greater than zero (0).", context);
        }
        return !negativeValue;
    }
}
