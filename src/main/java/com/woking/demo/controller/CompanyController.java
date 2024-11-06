package com.woking.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.woking.demo.config.SecurityUtils;
import com.woking.demo.dto.CompanyInfoDto;
import com.woking.demo.dto.UserDto;
import com.woking.demo.dto.UserInfoDto;
import com.woking.demo.entity.CompanyEntity;
import com.woking.demo.entity.RoleEntity;
import com.woking.demo.entity.UserEntity;
import com.woking.demo.exception.UserNotFoundException;
import com.woking.demo.mapper.RoleMapper;
import com.woking.demo.mapper.UserMapper;
import com.woking.demo.service.ApplyjobService;
import com.woking.demo.service.CompanyService;
import com.woking.demo.service.CvService;
import com.woking.demo.service.FileService;
import com.woking.demo.service.ImageService;
import com.woking.demo.service.RecruitmentService;
import com.woking.demo.service.RoleService;
import com.woking.demo.service.UserService;
import com.woking.demo.utils.ApplicationUtils;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/*
 * Company 
 * 1. The method profile use to see all information of company.
 * 2. The method updateUserProfile and updateCompany use to update infomation user info and company info @PostMapping.
 * 
 * 3. The method uploadLogo use to upload logo for company. One again i use method storedFileToLocal in fileService to upload file image 
 * 	and using <script> in profile.html have function upload(){.. using location.reload();} to reload the page making show company's logo
 * 	
 * 
 */

@Controller
@RequestMapping("/company")
@PreAuthorize("hasRole('ROLE_COMPANY')")
public class CompanyController {

	@Autowired
	CompanyService companyService;

	@Autowired
	UserService userService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	RoleService roleService;

	@Autowired
	RoleMapper roleMapper;

	@Autowired
	ApplicationUtils util;

	@Autowired
	ImageService imageService;

	@Autowired
	RecruitmentService recruitmentService;

	@Autowired
	ApplyjobService aService;

	@Autowired
	SecurityUtils securityUtil;

	@Autowired
	CvService cvService;

	@Autowired
	FileService fileService;

	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

	List<UserEntity> listUserFollower = new ArrayList<>();
	List<RoleEntity> roles = new ArrayList<>();

	// DOUBLE CHECK DONE
	@GetMapping("/profile")
	public String profile(Integer id, Model theModel, HttpSession session) {

		UserDto userAuthenticated = userService.getCurrentUserDto();
		UserInfoDto user = userService.getInfoUserDto(userAuthenticated.getId());
		CompanyInfoDto companyEntity = companyService.findCompanyInfoDtoById(userAuthenticated.getIdCompany());
		theModel.addAttribute("companyInformation", companyEntity);
		theModel.addAttribute("userInformation", user);
		session.setAttribute("user", userAuthenticated);
		session.setAttribute("company", companyEntity);

		return "public/user/profile";
	}

	/*
	 * Update CompanyEntity by companyDto.
	 * First, converting dto -> entity : In layer service, take all infomations of
	 * dto was edited and get it for entity.
	 * And then, using saveAndFlush entity.
	 */
	@PostMapping("/update")
	public String updateCompanyProfile(@Valid @ModelAttribute("companyInformation") CompanyInfoDto companyInformation,
			BindingResult bindingResult, Model theModel, RedirectAttributes redirectAttributes) {

		UserDto userAuthenticated = userService.getCurrentUserDto();
		UserInfoDto user = userService.getInfoUserDto(userAuthenticated.getId());
		if (bindingResult.hasErrors()) {
			theModel.addAttribute("companyInformation", companyInformation);
			theModel.addAttribute("userInformation", user);
			theModel.addAttribute("error", "Can not update informations");
			return "public/user/profile";
		}
		companyService.updateCompanyInfo(companyInformation, userAuthenticated);
		logger.info("Update company success " + companyInformation.getName());

		redirectAttributes.addFlashAttribute("success", "Update information success.");
		return "redirect:/company/profile";
	}

	@PostMapping("/uploadLogo/")
	public ResponseEntity<String> uploadLogo(@RequestParam("image") MultipartFile logo, Model theModel)
			throws IOException {

		UserDto userAuthenticated = userService.getCurrentUserDto();

		if (!imageService.isImage(logo)) {
			return ResponseEntity.ok("notAllow");
		}
		if (!imageService.checkSizeImage(logo)) {
			return ResponseEntity.ok("overSize");
		}

		CompanyEntity companyEntity = companyService.findCompanyEntityById(userAuthenticated.getIdCompany());

		if (companyEntity.getLogo() != null) {
			fileService.deleteFile(companyEntity.getLogo(), null);
		}

		companyService.setLogo(companyEntity, logo);

		return ResponseEntity.ok("true");

	}
}
