package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.config.SecurityUtils;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Applyjob;
import com.example.demo.entity.CompanyEntity;
import com.example.demo.entity.CvEntity;
import com.example.demo.entity.RecruitmentEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.ApplyjobService;
import com.example.demo.service.CompanyService;
import com.example.demo.service.CvService;
import com.example.demo.service.FileService;
import com.example.demo.service.RecruitmentService;
import com.example.demo.service.UserService;
import com.example.demo.utils.ApplicationUtils;

import jakarta.servlet.http.HttpSession;

/*
 * 1. The get-list method use to see all posts than user applied.
 * 2. The applyJob method use to get a cv have already to apply to the post. @PostMapping
 * 3. The applyJobWithFilePdf method to upload a new cv and get that to apply the post. @PostMapping
 * 		 If users need to update new cv with same a post they just need to upload cv again in that post with a new cv's name.
 * 
 * 4. The cancelApplied method use to delete Applyjob entity.
 * 		1.I did use checking current user by HttpSession. However i use a field User().getId() into Applyjob to check before delete it.  
 * 		2.If company role has confirmed that apply i set user role can't delete  Applyjob entity.
 * 	
 * 5. The getListPost method is page show all posts of a company. -> Not yet
 * 
 * 6. The accessApply method find Applypost entity and change status =1 (true)
 * 
 * BONUT: 
 * 	- All cvs that a user uploads needs to have a different name.
 * 	- 
 */

@Controller
@RequestMapping("/apply")
public class ApplyJobController {

	@Autowired
	UserService userService;

	@Autowired
	ApplyjobService aService;

	@Autowired
	RecruitmentService rService;

	@Autowired
	CvService cvService;

	@Autowired
	CompanyService companyService;

	@Autowired
	ApplicationUtils appUtil;

	@Autowired
	FileService fileService;

	private static final Logger logger = LoggerFactory.getLogger(ApplyJobController.class);

	// 1. DOUBLE CHECK DONE !
	@GetMapping("/get-list")
	public String applyJob(@RequestParam(value = "page", defaultValue = "1") Integer pageNo,
			RedirectAttributes redirectAttributes, Model theModel, HttpSession session) {

		String usename = SecurityUtils.getSessionUsername();

		if (usename != null) {
			UserDto userAuthenticated = userService.getCurrentUserDto();
			int pageSize = 5;
			theModel.addAttribute("user", userAuthenticated);
			session.setAttribute("user", userAuthenticated);
			Page<Applyjob> listAj = aService.listUserApplied(userAuthenticated.getId(), pageNo, pageSize);

			int numberPage = listAj.getTotalPages();
			int[] arr = appUtil.getNumberPagation(numberPage);

			theModel.addAttribute("arr", arr);
			theModel.addAttribute("numberPage", numberPage);
			theModel.addAttribute("applyList", listAj);
		}
		return "public/load/user/list-apply-job";
	}

	// 2. DOUBLE CHECK DONE !
	@PostMapping("/apply-job1/")
	public ResponseEntity<String> applyJob(@RequestParam("idRe") Integer idRe,
			@RequestParam(value = "text", required = false) String text, @RequestParam(value = "idCv") Integer idCv,
			Model theModel, HttpSession session, RedirectAttributes redirectAttributes) {

		String username = SecurityUtils.getSessionUsername();

		if (username == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("loginError"); // User not logged in
		}
		UserDto userAuthenticated = userService.getCurrentUserDto();
		RecruitmentEntity recruitment = rService.findRecruitmenById(idRe);

		try {

			UserEntity userEntity = userService.findById(userAuthenticated.getId());
			if (userService.existApplied(userEntity, idRe)) {
				return ResponseEntity.ok("false"); // Upload false
			}

			CvEntity cv = cvService.findById(idCv);
			aService.save(userEntity, cv.getName(), recruitment, text);
			logger.info("success apply cv name : " + cv.getName());
			return ResponseEntity.ok("true");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error"); // Generic error message
		}
	}

	// 3. DOUBLE CHECK DONE !
	@PostMapping("/apply-job/")
	public ResponseEntity<String> applyJobWithFilePdf(@RequestParam("idRe") Integer idRe,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "file") MultipartFile file, Model theModel, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String username = SecurityUtils.getSessionUsername();

		if (username == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("false"); // User not logged in
		}

		UserDto userAuthenticated = userService.getCurrentUserDto();
		try {
			if (file.isEmpty()) {
				return ResponseEntity.ok("choose");
			} else if (fileService.isExist(file, userAuthenticated.getId())) {
				return ResponseEntity.ok("exist");
			} else if (!fileService.isPdfFile(file)) {
				return ResponseEntity.ok("noPdf");
			}

			UserEntity userEntity = userService.findById(userAuthenticated.getId());
			Applyjob aj = aService.findApplyJobbyidUserdAndidRe(userAuthenticated.getId(), idRe);
			CvEntity newCv = cvService.storedCvToLocalAndDB(file, userAuthenticated.getId());
			userService.addNewCv(newCv, userEntity);

			if (aj == null) {
				RecruitmentEntity recruitment = rService.findRecruitmenById(idRe);
				aService.save(userEntity, newCv.getName(), recruitment, text);

			} else {
				String filename = StringUtils.cleanPath(file.getOriginalFilename());
				aj.setNameCv(filename);
				aj.setText(text);
				aService.save(aj);
				logger.info("success apply cv name :" + aj.getNameCv());
			}
			return ResponseEntity.ok("true");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error"); // Generic error message
		}
	}

	// 4.
	@GetMapping("/delete-apply/{idAj}")
	public String cancelApplied(@PathVariable("idAj") Integer idApply, RedirectAttributes redirectAttributes,
			Model theMode, HttpSession session) {

		String username = SecurityUtils.getSessionUsername();

		if (username == null) {
			return "errors/error-404";
		}

		UserDto userAuthenticated = userService.getCurrentUserDto();

		Applyjob applied = aService.findById(idApply);

		if (userAuthenticated.getId() != applied.getUser().getId()) {
			return "errors/error-404";
		}

		if (applied != null) {

			String nameRe = applied.getRecruitment().getTitle();

			System.out.println(applied.isStatus());

			if (!applied.isStatus()) {
				aService.delete(applied);
				redirectAttributes.addFlashAttribute("success", "Đã xóa thành công " + nameRe + ".");
			} else {
				redirectAttributes.addFlashAttribute("error", "Không thể xóa cv đã được duyệt " + nameRe + ".");
			}

		}

		return "redirect:/apply/get-list";
	}

	// Company use. Done
	@GetMapping("/list-user")
	public String getUserApplied(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			RedirectAttributes redirectAttributes, Model theModel, HttpSession session) {

		String username = SecurityUtils.getSessionUsername();

		if (username == null) {
			return "errors/error-404";
		}
		UserDto userAuthenticated = userService.getCurrentUserDto();

		int pageSize = 5;
		int idCompany = userAuthenticated.getIdCompany();

		CompanyEntity company = companyService.findCompanyEntityById(userAuthenticated.getIdCompany());

		theModel.addAttribute("company", company);

		Page<Applyjob> listApplies = aService.appliesOfUsersByCompanyId(idCompany, pageNo, pageSize);

		int numberPage = listApplies.getTotalPages();
		int[] arr = appUtil.getNumberPagation(numberPage);

		theModel.addAttribute("user", userAuthenticated);
		theModel.addAttribute("arr", arr);
		theModel.addAttribute("numberPage", numberPage);
		theModel.addAttribute("listApplies", listApplies);

		return "public/load/company-post/list-user";
	}

	// 6. DOUBLE CHECK DONE
	@PostMapping("/access")
	public ResponseEntity<String> accessApply(@RequestParam("idAj") Integer idAj, HttpSession session) {

		try {
			Applyjob aj = aService.findById(idAj);
			if (aj == null) {
				return ResponseEntity.ok("false");
			}
			if (aj.isStatus() == true) {
				return ResponseEntity.ok("exist");
			} else {
				aj.setStatus(true);
				aService.save(aj);
				return ResponseEntity.ok("true");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error"); // Generic error message
		}
	}
}
