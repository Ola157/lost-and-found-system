package hyk.springframework.lostandfoundsystem.validation;

import hyk.springframework.lostandfoundsystem.domain.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Htoo Yanant Khin
 **/
@Component
@Slf4j
public class PasswordMatchingValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.debug("Check password matching");
        User userDto = (User) target;
        
        // Check if confirmedPassword is empty
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmedPassword", "confirmedPassword.empty");
        
        // Check if passwords match
        if (userDto.getPassword() != null && userDto.getConfirmedPassword() != null) {
            if (!userDto.getPassword().equals(userDto.getConfirmedPassword())) {
                log.debug("Validate user input from view layer - Passwords do not match");
                errors.rejectValue("confirmedPassword", "confirmedPassword.mismatch");
            }
        }
    }
} 