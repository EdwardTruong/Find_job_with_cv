package com.example.demo.config.ValidationCustom;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;



public class MoneyFormatConstraintValidator implements ConstraintValidator<MoneyFormat, String> {
	private String formatSuffix;

	@Override
	public void initialize(MoneyFormat theCourse) {
		formatSuffix = theCourse.value();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean result;

		
		if(value != null) {
			result = value.endsWith(formatSuffix);
			System.out.println("result : "+ result);
		}
		else {
			result = true;
		}
		System.out.println("result : "+ result);
		return result;
	}
	
}
