package utils;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FormValidator {

    public static boolean emailValidator(String email) {

        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }


    /* Doesn't check the BO requirements of having:
    1. At least 1 digit
    2. At least 1 uppercase letter
    3. At least 1 symbol */
    public static boolean passwordValidator(String password) {

        if(password.length() < 8) {
            return false;
        }
        return true;
    }



}
