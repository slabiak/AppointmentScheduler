package com.example.slabiak.appointmentscheduler.validation;


import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsMatchesValidator implements ConstraintValidator<FieldsMatches, Object> {

    private String field;
    private String matchingField;

    @Override
    public void initialize(final FieldsMatches constraintAnnotation) {
        field = constraintAnnotation.field();
        matchingField = constraintAnnotation.matchingField();
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        Object field1Value = new BeanWrapperImpl(obj)
                .getPropertyValue(field);
        Object field2Value = new BeanWrapperImpl(obj)
                .getPropertyValue(matchingField);

        if (field1Value != null) {
            return field1Value.equals(field2Value);
        } else {
            return field2Value == null;
        }
    }

}
