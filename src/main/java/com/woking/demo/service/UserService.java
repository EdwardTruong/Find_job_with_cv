package com.woking.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.woking.demo.dto.UserDto;
import com.woking.demo.dto.UserInfoDto;
import com.woking.demo.entity.CvEntity;
import com.woking.demo.entity.UserEntity;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

/*
 * 1.The method getCurrentUserDto return UserDto get a user when they logined success. 
 * 		- I decided to create a userDto with the necessary information instead of using the UserDetailsCustom object.
 * 		- It is used only in the controller and not in other security layers.
 * 
 * 2. CRUD entity (user) methods:
 * 		- findAllUser()
 * 		- findUserEntityById(Integer id);
 * 		- saveNewUser(UserDto userDto);
 * 		- updateUser(UserEntity entity);
 * 		- deleteUserById(Integer id); 
 * 
 * 3. The methods for register new user :
 * 		- findUserByEmail(String email);
 * 		- emailExsit(UserDto userDto);
 * 
 * 
 * 4. The methods for functions of user:
 * 		- Setting avata of user and save into database with  @BLOB: setImageForUser(MultipartFile file, UserDto theUser); 
 * 			a.This method using  Base64.getEncoder().encodeToString(multipathFile.getBytes()) to save file for field have @BLOB above 
 * 			b.Loading  files - (image) in client using @{'data:image/png;base64,' + ${user.avatar}}"
 * 			c.I don't use this method in this project so i commeted it in interface UserService and class UserServiceImpl implements UserService	
 * 		
 * 		- Setting avata of user and save into local project : setImageForUserEntity(MultipartFile file, UserEntity theUser);
 * 			a.This method save a uri of user's image.
 * 			b.To loading file - (image) in client using Const_ALL_IMAGES_PATH_PATH + image's uri.
 * 			c.Finnaly, register the directory where images are uploaded in spring security.
 * 			d.I've implemented this approach in my current project.		
 * 
 * 		- Add new a CV to field list<Cv> of user: addNewCv(CvEntity cv, UserEntity userEntity);
 * 			 The funcion apply job need to use addNewCv method and storedCvToLocalAndDB method in CvService are complete.
 * 
 * 5. The findUserBySearchingBar method to find user(s)
 * 
 * 6. The sendEmailActiveUserStatus method use to send email to change field status of user object
 * 		 is true. 
 * 		- First step 	: Finding user with id and get user's email.
 * 		- Second 		: Using DataMailDto (main of a mail) to create title, context of mail.
 * 		- Thred 		: Using Map<String,Object> props to show infomation of user in email template				  
 * 		- Last 			: Using sendHtmlMail method in mailService.						
 * BONUTE : In this time no one can delete user in this project. 
 * 			I will make admin role to access status and manager roles later.
 * 			For now, user just need to click send email then user's status change to 1 (true)
 * 		
 */

public interface UserService {

	public UserDto getCurrentUserDto();

	List<UserDto> findAllUser();

	UserEntity findById(Integer id);

	String saveNewUser(UserDto userDto);

	void save(UserEntity user);

	void update(UserEntity entity);

	void updateUser(UserInfoDto userInformation, UserDto userAuthenticated);

	void deleteUserById(Integer id);

	UserEntity findUserByEmail(String email);

	boolean emailExsit(UserDto userDto);

	// boolean setImageForUser(MultipartFile file, UserEntity theUser);
	boolean setImageForUserEntity(MultipartFile file, UserEntity theUser) throws IOException;

	void addNewCv(CvEntity cv, UserEntity userEntity);

	Page<UserEntity> findUserBySearchingBar(String input, int pageNo, int pageSize);

	void sendEmailActiveUserStatus(UserDto dto) throws MessagingException;

	public UserInfoDto getInfoUserDto(Integer id);

}
