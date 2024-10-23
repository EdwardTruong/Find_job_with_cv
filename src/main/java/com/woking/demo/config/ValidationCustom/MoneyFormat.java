package com.woking.demo.config.ValidationCustom;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import jakarta.validation.Constraint;

@Constraint(validatedBy = MoneyFormatConstraintValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MoneyFormat {

	  // Define the default course code
    public String value() default " VND";

    // Define the default error message
    public String message() default "Kết thúc VND";

    // Define default groups
    public Class<?>[] groups() default {};

    // Define default payloads
    public Class<? extends jakarta.validation.Payload>[] payload() default {};
}
