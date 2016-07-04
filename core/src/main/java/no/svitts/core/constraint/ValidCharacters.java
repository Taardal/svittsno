package no.svitts.core.constraint;

import no.svitts.core.constraint.validator.CharacterValidator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = CharacterValidator.class)
public @interface ValidCharacters {

    String message() default "Could not validate characters.";

}
