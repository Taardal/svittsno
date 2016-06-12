package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

import static no.svitts.core.constraint.validator.MovieValidator.NAME_MAX_LENGTH;

public class NameValidator extends CoreValidator implements ConstraintValidator<ValidName, String> {

    @Override
    public void initialize(ValidName validName) {

    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return isNoViolations(getNameViolations(name), constraintValidatorContext);
    }

    private List<String> getNameViolations(String name) {
        List<String> violations = new ArrayList<>();
        if (name != null) {
            if (name.length() > NAME_MAX_LENGTH) {
                violations.add("Name was too long.");
            }
            if (isContainsIllegalCharacters(name)) {
                violations.add("Name contained illegal characters.");
            }
        }
        return violations;
    }

}
