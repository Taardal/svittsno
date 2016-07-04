package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.NonNegative;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NonNegativeValidator extends CoreValidator implements ConstraintValidator<NonNegative, Integer> {

    @Override
    public void initialize(NonNegative constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if (integer >= 0) {
            return true;
        } else {
            addConstraintViolation("Number is below 0 (zero). Must be equal to or greater than 0 (zero).", constraintValidatorContext);
            return false;
        }
    }
}
