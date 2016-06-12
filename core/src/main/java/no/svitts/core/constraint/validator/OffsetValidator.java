package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidOffset;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OffsetValidator extends CoreValidator implements ConstraintValidator<ValidOffset, Integer> {
    
    @Override
    public void initialize(ValidOffset constraintAnnotation) {
        
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        boolean negativeValue = value < 0;
        if (negativeValue) {
            addConstraintViolation("Offset was a negative number. Offset must be equal to or greater than zero (0).", context);
        }
        return !negativeValue;
    }
}
