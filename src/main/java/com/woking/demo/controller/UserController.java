package com.woking.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.woking.demo.config.SecurityUtils;
import com.woking.demo.dto.CompanyDto;
import com.woking.demo.dto.CvDto;
import com.woking.demo.dto.UserDto;
import com.woking.demo.entity.CompanyEntity;
import com.woking.demo.entity.CvEntity;
import com.woking.demo.entity.RecruitmentEntity;
import com.woking.demo.entity.RoleEntity;
import com.woking.demo.entity.UserEntity;
import com.woking.demo.exception.CvNotFoundException;
import com.woking.demo.mapper.CompanyMapper;
import com.woking.demo.mapper.CvMapper;
import com.woking.demo.mapper.RoleMapper;
import com.woking.demo.mapper.UserMapper;
import com.woking.demo.service.CompanyService;
import com.woking.demo.service.CvService;
import com.woking.demo.service.FileService;
import com.woking.demo.service.ImageService;
import com.woking.demo.service.RecruitmentService;
import com.woking.demo.service.RoleService;
import com.woking.demo.service.UserService;
import com.woking.demo.utils.ApplicationUtils;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/*
 * ***** IMPORTANT ******
 * 	WHEN RESTART DATABASE AFTER UPLOAD CV OR CHANGE IMAGES -(LOGO || AVATAR) REMEMBER CHECK FOLDERS : 'assets/cvs/' AND 'upload'PLEASE.
 * ***** IMPORTANT ******
 * 
 * 1. The userProfile method is user's info and load all file cv user have by MvcUriComponentsBuilder. 
 * 
 * 2. The uploadAvata method used to save image's name into database and load it by uri. @PostMapping
 * 		a.User only up load only 1 image use to avatar when they upload another one
 * 			the older image was delete.
 *
 * 3. The activeAccount method use to send user's e-mail address. (Real mail). @PostMapping
 * 		
 * 		b.The Server can't return instantly uri of user's field name avatar 
 * 		 	so i create script in profile.html function upload(){.. using location.reload();} to 
 * 			reload page when user update image success. I will find how to fix it later.
 * 		 
 * 
 * 4. The updateInfo method used to update user's info and have validation function.  @PostMapping
 *		***** IMPORTANT ******
 * 		- The tags <input type="hidden" th:value="list..." > of user's field lists can't can not update list related to user's information.
 * 			they replace all values of fields are lists of user object like a new list and some value of column in database COULD BE DELETE .
 * 		- So i create some lists (listUserSaved,listUserFollow,listUserApplied,roles) to save value. When user update infomation i just get those 
 * 			for fields missing.
 * 		***** IMPORTANT ******	
 * 	
 *
 * 		 		
 * 
 * 5. The storageNewCv method save user's cv (file). Only user role can upload file and cv's name is unique.
 * 6. The serveFile method use to download a file. 
 * 
 * 7. The method deleteFile(String filename, Integer userId); use to delete on disk.
 * 8. The method getPdf use to read cv in new tab.
 * 		-idCv in in url : @GetMapping("/pdf/{idCv}/{name}") use to check before return.
 * 			user role and company role can use this function. 
 * 			If user role used this function, idCv is field cv's id than user uploaded. url using(user/profile)
 * 			If company role used this function idCV always have value is 0. url using :(recruitment/detail/{id and apply/list-user}
 * 
 * 
 * 9. The method getListRecruitment is show page for all recruitments of a company.
 * 10.The method search use to find company by keyword.
 * 
 */

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	CvService cvService;

	@Autowired
	RoleService roleService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	CvMapper cvMapper;

	@Autowired
	RoleMapper roleMapper;

	@Autowired
	CompanyService companyService;

	@Autowired
	ApplicationUtils appUtil;

	@Autowired
	ImageService imageService;

	@Autowired
	RecruitmentService rService;

	@Autowired
	FileService fileService;
	
	@Autowired
	CompanyMapper companyMapper;

	List<RecruitmentEntity> listUserSaved = new ArrayList<>();
	List<CompanyEntity> listUserFollow = new ArrayList<>();
	List<RecruitmentEntity> listUserApplied = new ArrayList<>();
	List<RoleEntity> roles = new ArrayList<>();
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	// 1. DOUBLE CHECK DONE !
	@GetMapping(value = { "/profile" })
	public String userProfile(Model theModel, HttpSession session, RedirectAttributes redirectAttributes)
			throws IOException {

		UserDto userAuthenticated = userService.getCurrentUserDto();
		UserEntity user = userService.findById(userAuthenticated.getId());

		System.out.println("Check in from UserController : userProfile : + user.getAvatar() :" + user.getAvatar());

		session.setAttribute("user", userAuthenticated);
		theModel.addAttribute("user", userAuthenticated);
		theModel.addAttribute("userInformation", user);

		listUserApplied = user.getListAppliedOfUser();
		listUserFollow = user.getListCompanyFollower();
		listUserSaved = user.getListSavejob();
		roles = user.getAuthorities();
		for(RecruitmentEntity r : user.getListAppliedOfUser()) {
			logger.info("List applied of user :  "+ r.toString());
			
		}
		
		theModel.addAttribute("listCvEntity",user.getListCvEntity());
		
		List<CvDto> cvs = cvMapper.toListDto(user.getListCvEntity());
		if (!cvs.isEmpty()) {
			theModel.addAttribute("cvs", cvs);
		}
		fileService.initCreateLocalDisk(userAuthenticated.getId());
		userAuthenticated = userMapper.toDto(user);

		theModel.addAttribute("files", fileService.loadAll(userAuthenticated.getId()).map(path -> MvcUriComponentsBuilder
				.fromMethodName(UserController.class, "serveFile", path.getFileName().toString(), user.getId(), session)
				.build().toUri().toString()).collect(Collectors.toList()));
		return "public/user/profile";
	}

	// 2. DOUBLE CHECK DONE !
	@PostMapping("/uploadAvatar/")
	public ResponseEntity<String> uploadAvata(@RequestParam("image") MultipartFile avata, HttpSession session,
			RedirectAttributes redirect, Model theModel) throws IOException {

		UserDto userAuthenticated = userService.getCurrentUserDto();

		UserEntity user = userService.findById(userAuthenticated.getId());

		if (!imageService.isImage(avata)) {
			return ResponseEntity.ok("notAllow");
		}
		if (!imageService.checkSizeImage(avata)) {
			return ResponseEntity.ok("overSize");
		}

		if (user.getAvatar() != null) {
			fileService.deleteFile(user.getAvatar(), null);
		}

		userService.setImageForUserEntity(avata, user);

		return ResponseEntity.ok("true");
	}

	// 3.DOUBLE CHECK DONE !
	@PostMapping("/confirm-account")
	public String activeAccount(Model theModel, RedirectAttributes redirectAttributes) throws MessagingException {
		UserDto userAuthenticated = userService.getCurrentUserDto();
		userService.sendEmailActiveUserStatus(userAuthenticated);
		redirectAttributes.addFlashAttribute("msg_email_success", true);
		return "redirect:/auth/logout";
	}

	// 4.DOUBLE CHECK DONE !
	@PostMapping("/update")
	public String updateInfo(@Valid @ModelAttribute("userInformation") UserEntity userInformation,
			BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes, Model theModel) {

		UserDto userAuthenticated = userService.getCurrentUserDto();
		if (bindingResult.hasErrors()) {
			theModel.addAttribute("error", "Cập nhật thông tin không thành công.");
			return "public/user/profile";
		}
		
		userInformation.setId(userAuthenticated.getId());
		userInformation.setPassword(userAuthenticated.getPassword());
		userInformation.setStatus(userAuthenticated.isStatus());
		userInformation.setListAppliedOfUser(listUserApplied);
		userInformation.setListCompanyFollower(listUserFollow);
		userInformation.setListSavejob(listUserApplied);
		userInformation.setAuthorities(roles);
		userService.save(userInformation);
		redirectAttributes.addFlashAttribute("success", "Update information success.");
		return "redirect:/user/profile";
	}

	// 5. DOUBLE CHECK DONE !
	@PostMapping("/storageNewCv")
	public String storageNewCv(@RequestParam("file") MultipartFile file, HttpSession session, Model theModel,
			RedirectAttributes redirectAttributes) throws IOException {

		UserDto userAuthenticated = userService.getCurrentUserDto();

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Chọn Cv !");
			return "redirect:/user/profile";
		}

		if (!fileService.isPdfFile(file)) {
			redirectAttributes.addFlashAttribute("error", "Only file pdf :" + file.getOriginalFilename());
		} else if (fileService.isExist(file, userAuthenticated.getId())) {
			redirectAttributes.addFlashAttribute("error", "Exist file : " + file.getOriginalFilename());
		}

		UserEntity user = userService.findById(userAuthenticated.getId());
		CvEntity newCv = cvService.storedCvToLocalAndDB(file, userAuthenticated.getId());
		userService.addNewCv(newCv, user);
		logger.info("check in from storageNewCv : cv's name : " + newCv.getName());
		return "redirect:/user/profile";
	}

	// 6. DOUBLE CHECK DONE !
	@GetMapping("/files/{idUser}/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename,
			@PathVariable("idUser") Integer idUser, HttpSession session) {

		Resource file = fileService.loadAsResource(filename, idUser);
		if (file == null) {
			return ResponseEntity.notFound().build();	
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	// 7. DOUBLE CHECK DONE !
	@GetMapping("/deleteCv/{id}")
	public String deleteCv(@PathVariable("id") Integer cvId, HttpSession session,
			RedirectAttributes redirectAttributes) {

		UserDto userAuthenticated = userService.getCurrentUserDto();

		CvEntity cvEntity = cvService.findById(cvId);

		if (cvEntity == null) {
			throw new CvNotFoundException("Cv not found !");
		}

		if (cvEntity.getUser().getId() != userAuthenticated.getId()) {
			return "errors/error-404";
		}

		cvService.deleteCvEntity(cvEntity);
		fileService.deleteFile(cvEntity.getName(), userAuthenticated.getId());
		redirectAttributes.addFlashAttribute("success", "Delete cv success.");
		return "redirect:/user/profile";
	}

	// 8. DOUBLE CHECK DONE !
	@GetMapping("/pdf/{idCv}/{name}")
	public ResponseEntity<ByteArrayResource> getPdf(@PathVariable("name") String name,
			@PathVariable(value = "idCv", required = false) Integer idCv) throws IOException {
		CvEntity cvEntity = new CvEntity();

		if (idCv != 0) {
			cvEntity = cvService.findById(idCv);
		} else {
			cvEntity = cvService.findByName(name);
		}
		if (cvEntity == null) {
			return null;
		}
		String filename = cvEntity.getName();
		UserEntity userEntity = cvEntity.getUser();
		Resource resource = fileService.loadAsResource(filename, userEntity.getId());
		byte[] pdfContent = resource.getContentAsByteArray();
		ByteArrayResource byteResource = new ByteArrayResource(pdfContent);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=example.pdf")
				.contentType(MediaType.APPLICATION_PDF).contentLength(pdfContent.length).body(byteResource);
	}

	// 9. DOUBLE CHECK DONE !
	@GetMapping("/company-posts/{companyId}")
	public String getListRecruitment(@RequestParam(value = "page", defaultValue = "1") Integer pageNo,
			@PathVariable("companyId") Integer companyId, RedirectAttributes redirectAttributes, Model theModel,
			HttpSession session) {

		String username = SecurityUtils.getSessionUsername();
		if (username != null) {
			UserDto userAuthenticated = userService.getCurrentUserDto();
			theModel.addAttribute("user", userAuthenticated);

			if (!roleService.isCompany(userAuthenticated)) {
				List<CvDto> cvs = cvService.findCvByUserId(userAuthenticated.getId());
				theModel.addAttribute("cvs", cvs);
			}
		}

		int pageSize = 5;
		CompanyDto companyDto = companyService.findCompanyDtoByIdToGetDetail(companyId);
		Page<RecruitmentEntity> recruimenstDtoPaging = rService.listAllRecruitmentPageable(companyId, pageNo, pageSize);

		int numberPage = recruimenstDtoPaging.getTotalPages();
		int[] arr = appUtil.getNumberPagation(numberPage);

		theModel.addAttribute("arr", arr);
		theModel.addAttribute("numberPage", numberPage);
		theModel.addAttribute("recruitmentList", recruimenstDtoPaging);
		theModel.addAttribute("list", recruimenstDtoPaging);

		theModel.addAttribute("company", companyDto);

		return "public/load/user/post-company";
	}

	@GetMapping("/search")
	public String search(@RequestParam(value = "keySearch", required = false) String keySearch, Model theModel,
			HttpSession session, @RequestParam(value = "page", defaultValue = "1") Integer pageNo) {

		String username = SecurityUtils.getSessionUsername();
		if (username != null) {

			UserDto userAuthenticated = userService.getCurrentUserDto();
			theModel.addAttribute("user", userAuthenticated);
		}

		int pageSize = 5;
		Page<CompanyDto> companiesDto = companyService.findByKeyword(keySearch, pageNo, pageSize);		
		theModel.addAttribute("list", companiesDto);

		int numberPage = companiesDto.getTotalPages();
		int[] arr = appUtil.getNumberPagation(numberPage);
		theModel.addAttribute("numberPage", numberPage);
		theModel.addAttribute("list", companiesDto);
		theModel.addAttribute("arr", arr);
		theModel.addAttribute("keySearch", keySearch);
		return "public/load/result-search-user";

	}
}