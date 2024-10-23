package com.woking.demo.mapper;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.woking.demo.dao.RoleDao;
import com.woking.demo.dto.UserDto;
import com.woking.demo.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class UserMapper {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	RoleDao roleDao;

	@Autowired
	RoleMapper roleMapper;

	 @Autowired
	 CvMapper cvMapper;

	 /*
	  * First step login success to reach convert from entity -> UserDTO 
	  */
	public UserDto toDto(UserEntity entity) {
		UserDto dto = new UserDto();
		dto.setId(entity.getId());
		dto.setEmail(entity.getEmail());
		dto.setFullName(entity.getFullName());
		dto.setPassword(entity.getPassword());
		dto.setStatus(entity.isStatus());
		dto.setAddress(entity.getAddress());
		dto.setPhoneNumber(entity.getPhoneNumber());
		dto.setDescription(entity.getDescription());
		if(entity.getAvatar() != null){
			dto.setAvatar(entity.getAvatar());
		}

		dto.setRole(entity.getAuthorities().get(0).getId()); 
		if(entity.getCompanyEntity() != null){
			dto.setIdCompany(entity.getCompanyEntity().getId());
		}
		return dto;
	 }

	public List<UserDto> listUserEntityToDto(List<UserEntity> listUserEntity){
		return listUserEntity.stream().map(entity->toDto(entity)).toList();
	}
 
	 public UserEntity toEntity(UserDto dto) {
		UserEntity entity = new UserEntity();
		entity.setId(dto.getId());
		entity.setAddress(dto.getAddress()); 
		entity.setDescription(dto.getDescription());
		entity.setEmail(dto.getEmail());
		entity.setFullName(dto.getFullName());
		entity.setPassword(dto.getPassword());
		entity.setPhoneNumber(dto.getPhoneNumber());
		entity.setStatus(dto.isStatus());
		 return entity;
	 }

	public List<UserEntity> listToEntity(List<UserDto> listUserDto){
		return listUserDto.stream().map(dto->toEntity(dto)).toList();
	}
	 

}
