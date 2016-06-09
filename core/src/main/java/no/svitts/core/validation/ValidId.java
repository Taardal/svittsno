package no.svitts.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = IdValidator.class)
public @interface ValidId {

    String message() default "ID was invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
