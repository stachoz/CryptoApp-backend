package com.example.cryptoapp.validators;

import com.example.cryptoapp.crypto.transaction.TransactionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<Enum, String> {

    private Class<? extends java.lang.Enum<?>> enumClass;

    @Override
    public void initialize(Enum constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(enumClass.getEnumConstants()).anyMatch(
                enumValue -> enumValue.name().equals(s)
        );
    }
}
