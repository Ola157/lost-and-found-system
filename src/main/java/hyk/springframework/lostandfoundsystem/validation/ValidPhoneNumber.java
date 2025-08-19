package hyk.springframework.lostandfoundsystem.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface ValidPhoneNumber {

    String message() default "Enter valid +1 phone number. (Eg. +1-555-123-4567, +1 555 123 4567, +15551234567)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}