package no.svitts.core.constraint;

import no.svitts.core.constraint.validator.NotNullOrEmptyValidator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = NotNullOrEmptyValidator.class)
public @interface NotNullOrEmpty {

    String message() default "Could not validate string.";

}
