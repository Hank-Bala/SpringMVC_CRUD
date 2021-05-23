package _01.validate;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import _01.model.CustomerBean;

public class CustomerValidator implements Validator {
	private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w\\d._-]+@[\\w\\d.-]+\\.[\\w\\d]{2,6}$");

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerBean.class.isAssignableFrom(clazz);
	}
//
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "customerBean.name.empty", "姓名欄位不正確-05-20");
		ValidationUtils.rejectIfEmpty(errors, "password", "customerBean.password.empty");
		ValidationUtils.rejectIfEmpty(errors, "password1", "customerBean.password1.empty");
		ValidationUtils.rejectIfEmpty(errors, "email", "customerBean.email.empty", "email欄不能空白");

		CustomerBean customer = (CustomerBean) target;
		
		if (!errors.hasFieldErrors("name")) {
			if (customer.getName() != null && customer.getName().length() < 2 || customer.getName().length() > 30) {
				errors.rejectValue("name", "customerBean.name.size");
			}	
		}

		if (customer.getPassword() != null && customer.getPassword().contains(" ")) {
			errors.rejectValue("password", "customerBean.password.space");
		}

		if (customer.getPassword1() != null && customer.getPassword1().contains(" ")) {
			errors.rejectValue("password1", "customerBean.password1.space");
		}
		
		if (customer.getPassword1() != null && customer.getPassword1().length() < 5 && customer.getPassword1().length() > 15) {
			errors.rejectValue("password1", "customerBean.password1.size");
		}
		
		if (customer.getPassword1() != null && customer.getPassword() != null &&
			!customer.getPassword1().equals(customer.getPassword())
		) {
			errors.rejectValue("password", "customerBean.password.mustEqual");
		}
		
		if (!errors.hasFieldErrors("email")) {
			if (customer.getEmail() != null && !EMAIL_REGEX.matcher(customer.getEmail()).matches()) {
				errors.rejectValue("email", "customerBean.email.invalid");
			}
		}
	}
}
