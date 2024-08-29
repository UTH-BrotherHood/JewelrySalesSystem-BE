package com.example.JewelrySalesSystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class GenericValidator implements ConstraintValidator<GenericConstraint, Object> {

    private String pattern;
    private int min;
    private int max;

    @Override
    public void initialize(GenericConstraint constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) return true;

        // Example: Add custom logic for validation
        if (value instanceof String) {
            String stringValue = (String) value;
            if (!pattern.isEmpty() && !stringValue.matches(pattern)) return false;
            if (stringValue.length() < min || stringValue.length() > max) return false;
        }

        if (value instanceof Number) {
            Number numberValue = (Number) value;
            if (numberValue.intValue() < min || numberValue.intValue() > max) return false;
        }

        return true;
    }
}
