package _02.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import _02.model.User;
@Component
public class EmailValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}
	@Override
	public void validate(Object target, Errors errors) {
		User user = (User)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "","Email is empty");
		if (!user.getEmail().contains("@")) {
			errors.rejectValue("email","", "Email is not valid.");
		}
	}
} 