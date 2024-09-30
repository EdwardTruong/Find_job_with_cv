package com.example.demo.service;

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

import com.example.demo.config.SecurityUtils;
import com.example.demo.dao.CompanyDao;
import com.example.demo.dao.RoleDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.CompanyDto;
import com.example.demo.dto.DataMailDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Applyjob;
import com.example.demo.entity.CompanyEntity;
import com.example.demo.entity.CvEntity;
import com.example.demo.entity.FollowCompany;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.SaveJob;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.CompanyMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.ApplicationUtils;
import com.example.demo.utils.Const;
import com.example.demo.utils.Const.UNIT;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
@Transactional
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
	public void save(UserEntity user) {
		userDao.save(user);
	}

	@Override
	public String saveNewUser(UserDto userDto) {
		userDto.setId(0);
		userDto.setAddress("User Address");
		userDto.setPhoneNumber("090");

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
	public void deleteUserById(Integer id) {
		userDao.deleteById(id);
	}

	@Override
	public void updateUser(UserEntity entity) {
		userDao.saveAndFlush(entity);
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
	public void addNewCv(CvEntity newCvEntity, UserEntity userEntity) {
		userEntity.addCv(newCvEntity);
		updateUser(userEntity);
		cvService.upLoadteCv(newCvEntity);
	}

	@Override
	public UserEntity findById(Integer id) {
		Optional<UserEntity> result = userDao.findById(id);
		return result.orElseThrow(null);
	}

	@Override
	public boolean setImageForUserEntity(MultipartFile file, UserEntity theUser) throws IOException {

		String nameImage = fileService.storedFileToLocal(file, null);

		theUser.setAvatar(nameImage);

		updateUser(theUser);

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
	public boolean existSavedJob(UserEntity user, int idRe) {
		for (SaveJob sjs : user.getListSavejob()) {
			if (sjs.getRecruitment().getId() == idRe) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean existFollowCompany(UserEntity user, int idCompany) {
		for (FollowCompany fl : user.getListCompanyFollower()) {
			if (fl.getCompany().getId() == idCompany) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean existApplied(UserEntity user, int idRe) {
		for (Applyjob aj : user.getListAppliedOfUser()) {
			if (aj.getRecruitment().getId() == idRe) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int passworRegisterdErrors(String password) {
		boolean isNumberInside = false;
		boolean isLowerCaseInside = false;
		boolean isUpperCaseInside = false;
		boolean isSpecialCharacters = false;
		int count = 0;

		for (Character ch : password.toCharArray()) {
			if (Character.isLowerCase(ch))
				isLowerCaseInside = true;
			else if (Character.isUpperCase(ch))
				isUpperCaseInside = true;
			else if (Character.isDigit(ch))
				isNumberInside = true;
			else if (UNIT.SPECIAL_CHACTERLIST.contains(ch))
				isSpecialCharacters = true;
		}

		if (!isNumberInside)
			count++;
		if (!isLowerCaseInside)
			count++;
		if (!isUpperCaseInside)
			count++;
		if (!isSpecialCharacters)
			count++;

		return Math.max(count, 8 - password.length());
	}

}
