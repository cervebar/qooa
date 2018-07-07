package name.babu.qooa.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import name.babu.qooa.user.DTOUser;
import name.babu.qooa.user.UserService;

@Component
public class RegistrationValidator implements Validator {

  private static final int MIN_USERNAME_LENGH = 2;
  private static final int MAX_USERNAME_LENGH = 32;
  private static final int MIN_PASSWORD_LENGH = 2;
  private static final int MAX_PASSWORD_LENGH = 32;
  // TODO solve better

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return DTOUser.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DTOUser user = (DTOUser) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
    if (user.getUsername().length() < MIN_USERNAME_LENGH || user.getUsername().length() > MAX_USERNAME_LENGH) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }
    // TODO email exists
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
    if (user.getPassword().length() < MIN_PASSWORD_LENGH || user.getPassword().length() > MAX_PASSWORD_LENGH) {
            errors.rejectValue("password", "Size.userForm.password");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "NotEmpty");
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}