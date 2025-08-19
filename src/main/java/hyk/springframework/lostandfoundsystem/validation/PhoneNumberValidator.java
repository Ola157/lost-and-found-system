package hyk.springframework.lostandfoundsystem.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    /*
     * Updated to accept +1 phone numbers (US/Canada)
     * Examples:
     * +1-555-123-4567
     * +1 555 123 4567
     * +15551234567
     * 1-555-123-4567
     * 1 555 123 4567
     * 15551234567
     */
    private static final String PHONE_NUMBER_PATTERN = "^\\+?1[-\\s]?\\(?([0-9]{3})\\)?[-\\s]?([0-9]{3})[-\\s]?([0-9]{4})$";
    private static final Pattern PATTERN = Pattern.compile(PHONE_NUMBER_PATTERN);

    @Override
    public boolean isValid(final String phoneNumber, final ConstraintValidatorContext context) {
        log.debug("Validate phone number");
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return (validatePhoneNumber(phoneNumber.trim()));
    }

    private boolean validatePhoneNumber(final String phoneNumber) {
        Matcher matcher = PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }
}
