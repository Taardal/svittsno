package no.svitts.core.constraint;

import no.svitts.core.constraint.validator.CharacterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = CharacterValidator.class)
public @interface ValidCharacters {

    String message() default "Could not validate characters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
