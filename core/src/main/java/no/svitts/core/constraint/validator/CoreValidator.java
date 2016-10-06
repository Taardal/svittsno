package no.svitts.core.constraint.validator;

import javax.validation.ConstraintValidatorContext;
import java.util.List;

abstract class CoreValidator {

    protected boolean isNoViolations(List<String> violations, ConstraintValidatorContext constraintValidatorContext) {
        if (violations.size() == 0) {
            return true;
        } else {
            for (String violation : violations) {
                addConstraintViolation(violation, constraintValidatorContext);
            }
            return false;
        }
    }

    void addConstraintViolation(String message, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

}
