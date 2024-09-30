package com.example.demo.config.ValidationCustom;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = PasswordMatchesValidation.class)
public @interface PasswordMatches {

	 public String value() default "Mật khẩu không khớp !";
		
	 public Class<?>[] groub() default{};
	 
	 public Class<? extends Payload>[] payload() default{};
}
