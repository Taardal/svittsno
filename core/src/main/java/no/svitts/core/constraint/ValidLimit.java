package no.svitts.core.constraint;

import no.svitts.core.constraint.validator.LimitValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = LimitValidator.class)
public @interface ValidLimit {

    String message() default "Could not validate limit.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
