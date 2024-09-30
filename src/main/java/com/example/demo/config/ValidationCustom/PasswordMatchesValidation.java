package com.example.demo.config.ValidationCustom;

import com.example.demo.dto.UserDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidation implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		UserDto userDto = (UserDto)value;
		return userDto.getPassword().equals(userDto.getRePassword());
	}

}
