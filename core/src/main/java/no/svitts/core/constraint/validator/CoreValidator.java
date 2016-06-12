package no.svitts.core.constraint.validator;

import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    boolean isContainsIllegalCharacters(String string) {
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"_^£\\$€´]");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

}
