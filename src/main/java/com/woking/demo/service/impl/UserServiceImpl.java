package com.woking.demo.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.woking.demo.config.SecurityUtils;
import com.woking.demo.dao.CompanyDao;
import com.woking.demo.dao.RoleDao;
import com.woking.demo.dao.UserDao;
import com.woking.demo.dto.CompanyDto;
import com.woking.demo.dto.DataMailDto;
import com.woking.demo.dto.UserDto;
import com.woking.demo.dto.UserInfoDto;
import com.woking.demo.entity.CompanyEntity;
import com.woking.demo.entity.CvEntity;
import com.woking.demo.entity.RoleEntity;
import com.woking.demo.entity.UserEntity;
import com.woking.demo.mapper.CompanyMapper;
import com.woking.demo.mapper.RoleMapper;
import com.woking.demo.mapper.UserMapper;
import com.woking.demo.service.CompanyService;
import com.woking.demo.service.CvService;
import com.woking.demo.service.FileService;
import com.woking.demo.service.ImageService;
import com.woking.demo.service.MailService;
import com.woking.demo.service.RecruitmentService;
import com.woking.demo.service.RoleService;
import com.woking.demo.service.UserService;
import com.woking.demo.utils.ApplicationUtils;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	UserDao userDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	RoleMapper roleMapper;

	@Autowired
	RoleService roleService;

	@Autowired
	CompanyService companyService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	CompanyMapper companyMapper;

	@Autowired
	CompanyDao companyDao;

	@Autowired
	CvService cvService;

	@Autowired
	FileService fileService;

	@Autowired
	ApplicationUtils util;

	@Autowired
	MailService mailService;

	@Autowired
	RecruitmentService rService;

	@Autowired
	ImageService imageService;

	@Override
	public UserDto getCurrentUserDto() {
		UserDto dto = new UserDto();
		dto.setEmail(SecurityUtils.getPrincipal().getUsername());
		dto.setId((Integer) SecurityUtils.getPrincipal().getId());
		dto.setFullName(SecurityUtils.getPrincipal().getFullName());
		dto.setIdCompany(SecurityUtils.getPrincipal().getIdCompany());
		dto.setStatus(SecurityUtils.getPrincipal().isStatus());
		dto.setIdCompany(SecurityUtils.getPrincipal().getIdCompany());
		dto.setNameCompany(SecurityUtils.getPrincipal().getNameCompany());
		dto.setPassword(SecurityUtils.getPrincipal().getPassword());
		dto.setAvatar(null);
		return dto;
	}

	@Override
	public List<UserDto> findAllUser() {
		return userMapper.listUserEntityToDto(userDao.findAll());
	}

	@Override
	@Transactional
	public void save(UserEntity user) {
		userDao.save(user);
	}

	@Override
	@Transactional
	public String saveNewUser(UserDto userDto) {
		userDto.setId(0);
		userDto.setAddress("User Address");
		userDto.setPhoneNumber("090");
		// tạm thời set giá trị mặt định là true để có thể test tạo user mới.
		userDto.setStatus(false);
		UserEntity tempEntity = userMapper.toEntity(userDto);

		Optional<RoleEntity> result = roleDao.findById(userDto.getRole());
		if (result.isPresent()) {
			RoleEntity role = result.get();
			tempEntity.addRole(role);
		}
		tempEntity.setPassword(passwordEncoder.encode(tempEntity.getPassword()));
		if (userDto.getRole() == 2) {
			CompanyDto newCompanyDto = new CompanyDto(null, "Email@example.com", "Company's name", "Company's Address",
					"090", "", null, true, tempEntity.getId(), null, null);
			CompanyEntity newCompany = companyMapper.toEntity(newCompanyDto);

			newCompany.setUser(tempEntity);
			tempEntity.setCompanyEntity(newCompany);
			companyDao.save(newCompany);
		} else {
			fileService.initCreateLocalDisk(tempEntity.getId());

		}
		UserEntity userEntity = userDao.save(tempEntity);
		return userEntity.getFullName();
	}

	@Override
	@Transactional
	public void deleteUserById(Integer id) {
		userDao.deleteById(id);
	}

	@Override
	@Transactional
	public void update(UserEntity entity) {
		userDao.saveAndFlush(entity);
	}

	@Override
	public void updateUser(UserInfoDto dtoEdited, UserDto userAuthenticated) {
		UserEntity current = this.findById(userAuthenticated.getId());
		current.setEmail(dtoEdited.getEmail());
		current.setFullName(dtoEdited.getFullName());
		current.setAddress(dtoEdited.getAddress());
		current.setPhoneNumber(dtoEdited.getPhoneNumber());
		current.setDescription(dtoEdited.getDescription());
		current.setAvatar(dtoEdited.getAvatar());
		this.update(current);
	}

	@Override
	public UserEntity findUserByEmail(String email) {
		Optional<UserEntity> object = userDao.findUserByEmail(email);
		return object.orElse(null);
	}

	@Override
	public boolean emailExsit(UserDto userDto) {
		if (findUserByEmail(userDto.getEmail()) != null) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public void addNewCv(CvEntity newCvEntity, UserEntity userEntity) {
		userEntity.addCv(newCvEntity);
		update(userEntity);
		cvService.upLoadteCv(newCvEntity);
	}

	@Override
	public UserEntity findById(Integer id) {
		Optional<UserEntity> result = userDao.findById(id);
		return result.orElseThrow(null);
	}

	@Override
	@Transactional
	public boolean setImageForUserEntity(MultipartFile file, UserEntity theUser) throws IOException {

		String nameImage = fileService.storedFileToLocal(file, null);

		theUser.setAvatar(nameImage);

		update(theUser);

		return true;
	}

	@Override
	public void sendEmailActiveUserStatus(UserDto dto) throws MessagingException {
		DataMailDto dataMail = new DataMailDto();

		UserEntity entity = findById(dto.getId());
		entity.setStatus(true);
		this.save(entity);
		dataMail.setTo(entity.getEmail());
		dataMail.setSubject("Active account.");

		Map<String, Object> props = new HashMap<>();
		props.put("name", entity.getFullName());
		props.put("status", entity.isStatus());

		dataMail.setProps(props);

		mailService.sendHtmlMail(dataMail, "public/load/email-template");

	}

	@Override
	public Page<UserEntity> findUserBySearchingBar(String input, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return userDao.searchingUser(input, pageable);

	}

	@Override
	public UserInfoDto getInfoUserDto(Integer id) {
		UserEntity user = this.findById(id);
		return userMapper.toInfoDto(user);
	}

	// @Override
	// public boolean setImageForUser(MultipartFile file, UserEntity theUser) {
	// try {
	// String logo = Base64.getEncoder().encodeToString(file.getBytes());
	// theUser.setAvatar(logo);
	// updateUser(theUser);
	// return true;
	// } catch (IOException e) {
	// throw new RuntimeException("Can't upload image.");
	// }
	// }

}
