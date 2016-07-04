package no.svitts.core.constraint;

import no.svitts.core.constraint.validator.LengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = LengthValidator.class)
public @interface Length {

    String message() default "Could not validate length.";

    int length() default 255;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
