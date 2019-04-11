package com.example.slabiak.appointmentscheduler.validation;


import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsMatchesValidator implements ConstraintValidator<FieldsMatches, Object> {

    private String field1;
    private String field2;

    @Override
    public void initialize(final FieldsMatches constraintAnnotation) {
        field1 = constraintAnnotation.field1();
        field2 = constraintAnnotation.field2();
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
            Object field1Value = new BeanWrapperImpl(obj)
                    .getPropertyValue(field1);
            Object field2Value = new BeanWrapperImpl(obj)
                    .getPropertyValue(field2);

            if (field1Value != null) {
                return field1Value.equals(field2Value);
            } else {
                return field2Value == null;
            }
        }

}
