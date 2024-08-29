package com.example.JewelrySalesSystem.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {GenericValidator.class})
public @interface GenericConstraint {
    String message() default "Invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // Add any attributes you want to pass to the validator
    String pattern() default "";  // For regex patterns
    int min() default Integer.MIN_VALUE;
    int max() default Integer.MAX_VALUE;
}
