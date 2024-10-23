package com.woking.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/*
 * 1. I use UserDto into register page so it have valiadation some fieldes. 
 * 2. Fieldes :  idCompany, nameCompany use to convert for UserDetailCustomer in spring security.
 * 3. Fieldes : saveJobId and followId use to save those ids when user access posts. 
 * 	The purpose is to increase user experience.
 */

@Setter
@Getter
@ToString
public class UserDto {
    
	
	private Integer id;

	private String address;
	
	@Email(message = "Email không đúng định dạng !", regexp = "^[\\w-\\+]+(\\.[\\w-\\+]+)*@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,6}$")
	private String email;
	
	@Size(min=5, message = "Độ dài mật khẩu quá ngắn. Ít nhất là 5 ký tự.")
	private String password;
	
	@NotBlank(message = "Yêu cầu nhập lại mật khẩu.")
	private String rePassword;

	@NotBlank(message = "Yêu cầu nhập họ và tên.")
	private String fullName;
	
	private boolean status;

	private String avatar;

	private String description;

	private String phoneNumber;
	
	@Min(value = 1,message = "Yêu cầu vai trò.")
	private int role;

	private Integer idCompany;

	private String nameCompany;

	private int saveJobId;

	private int followId;
	
}
