package no.svitts.core.constraint.validator;

import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class CoreValidator {

    void addConstraintViolation(String message, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

    boolean containsIllegalCharacters(String string) {
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"_^£\\$€´]");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

}
