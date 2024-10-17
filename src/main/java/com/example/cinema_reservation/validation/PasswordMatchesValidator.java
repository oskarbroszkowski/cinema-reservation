package com.example.cinema_reservation.validation;

import com.example.cinema_reservation.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        User user = (User) object;

        if (user.getPassword().startsWith("$2a$")) {
            return true;
        }

        return user.getPassword().equals(user.getMatchingPassword());
    }
}
