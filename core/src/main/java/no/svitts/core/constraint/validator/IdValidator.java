package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

import static no.svitts.core.movie.Movie.ID_MAX_LENGTH;

public class IdValidator extends CoreValidator implements ConstraintValidator<ValidId, String> {

    @Override
    public void initialize(ValidId validId) {

    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        return isNoViolations(getIdViolations(id), constraintValidatorContext);
    }

    private List<String> getIdViolations(String id) {
        List<String> violations = new ArrayList<>();
        if (id != null && !id.equals("")) {
            if (id.length() > ID_MAX_LENGTH) {
                violations.add("ID was too long.");
            }
            if (isContainsIllegalCharacters(id)) {
                violations.add("ID contained illegal characters.");
            }
        } else {
            violations.add("ID was missing.");
        }
        return violations;
    }

}
