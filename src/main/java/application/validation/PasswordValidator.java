package application.validation;

import application.dto.user.UserRequestRegistrationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordsAreEqual, Object> {
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        UserRequestRegistrationDto user = (UserRequestRegistrationDto) object;
        return user.getPassword().equals(user.getRepeatPassword());
    }
}
