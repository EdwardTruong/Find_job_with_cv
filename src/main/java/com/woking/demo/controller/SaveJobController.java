package com.woking.demo.controller;

/*
 * 
 * 3.The deleteSaveJob method used to delete a job user saved. 
 * 		
 * 
 * 		
 * 	
 * BONUTE : In I set field saveJobId of object UserDTO in recruitment/detail/{id} to make different return 
 */

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.woking.demo.config.SecurityUtils;
import com.woking.demo.dto.CvDto;
import com.woking.demo.dto.UserDto;
import com.woking.demo.entity.RecruitmentEntity;
import com.woking.demo.entity.SaveJob;
import com.woking.demo.entity.UserEntity;
import com.woking.demo.mapper.UserMapper;
import com.woking.demo.service.CvService;
import com.woking.demo.service.RecruitmentService;
import com.woking.demo.service.SavejobService;
import com.woking.demo.service.UserService;
import com.woking.demo.utils.ApplicationUtils;

import jakarta.servlet.http.HttpSession;

/*
 * 1. The method listSaveJobs use to see all company's posts than user role saved. 
 * 		When company role access the page, it is going to empty because it has been configured in security  - SecurityConfigurer.java
 *
 * 2. The method saveJob use to save company's posts. The main function use to manage all post then user saved and see it again. @PostMapping
 * 		I didn't create the query to find a post by idUser and idRecuritment to check user saved. 
 * 		I use to user.getListSavejob() to do it.
 * 
 * 3.  The method deleteSaveJob use to delete a post than user saved. 
 * 		I use to HttpSession.getAttribute("user") to check the current user or not. If it's true,
 * 			user can delete SaveJob entity.
 * 		I use a field SaveJobId to return 2 type.
 * 			1. If users in list-save-job page they were return back list-save-job page;
 * 			2. If users in a post they were return back that post.
 * 
 */

@Controller
@RequestMapping("/save-job")
public class SaveJobController {

	@Autowired
	RecruitmentService rService;

	@Autowired
	SavejobService sService;

	@Autowired
	UserService userService;

	@Autowired
	CvService cvService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	ApplicationUtils utils;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/get-list")
	public String listSaveJobs(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			RedirectAttributes redirectAttributes, Model theModel, HttpSession session) {

		UserDto userAuthenticated = userService.getCurrentUserDto();
		theModel.addAttribute("user", userAuthenticated);
		logger.info(userAuthenticated.getNameCompany());

		int PageSize = 5;
		Page<SaveJob> saveJobListPagination = sService.findAllJobSavedByUserId(userAuthenticated.getId(),pageNo,PageSize);
		List<CvDto> cvs = cvService.findCvByUserId(userAuthenticated.getId());
		int numberPage = saveJobListPagination.getTotalPages();
		int[] arr = utils.getNumberPagation(numberPage);

		theModel.addAttribute("cvs", cvs);
		theModel.addAttribute("recruitmentList", arr);
		theModel.addAttribute("numberPage", numberPage);
		theModel.addAttribute("saveJobList", saveJobListPagination);
		return "public/load/user/list-save-job";
	}


	@PostMapping("/save/{idRe}")
	public ResponseEntity<String> saveJob(@RequestParam("idRe") Integer idRe, HttpSession session) {

		String username = SecurityUtils.getSessionUsername();

		if (username == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("error"); // User not logged in
		}

		UserDto userAuthenticated = userService.getCurrentUserDto();

		UserEntity uEntity = userService.findById(userAuthenticated.getId());

		List<RecruitmentEntity> listUserSave = uEntity.getListSavejob();

		for (RecruitmentEntity r : listUserSave) {
			if (r.getId() == idRe) {
				return ResponseEntity.ok("false"); // Save job unsuccessful
			}
		}

		RecruitmentEntity rEntity = rService.findRecruitmenById(idRe);

		sService.save(uEntity, rEntity);
		return ResponseEntity.ok("true"); // Follow successful

	}

	/*
	 * - Check security
	 * - Check saveJob exist.
	 * - setVIew of recruitment.
	 *  	If user deleted save_job into recruitmentDetail of RecruitmentController, the view of recruitment entity minus 1(-1).
	 *  	When user go back to recruitmentDetail,the method always plus 1(+1) it mean nothing happend with view of recruitment.
	 *  	
	 * - Delete entity.
	 */
	@GetMapping("/delete/{id}")
	public String deleteSaveJob(@PathVariable("id") Integer idSj, Model theModel, HttpSession session,
			RedirectAttributes redirectAttributes) {

		String usename = SecurityUtils.getSessionUsername();
		
		if(usename == null) {
			return "errors/error-404";
		}
		
		UserDto dto = (UserDto)session.getAttribute("user");

		SaveJob sj = sService.findById(idSj);

		if(sj == null) {
			return "errors/error-404";
		}
		if(sj.getUser().getId() != dto.getId()){
			return "errors/error-404";
		}

		if (dto.getSaveJobId() != 0 && dto.getSaveJobId() == sj.getId()) {
			int idRe = sj.getRecruitment().getId();
			RecruitmentEntity recruitment = sj.getRecruitment();
			recruitment.setView(recruitment.getView() - 1);
			rService.update(recruitment);
			sService.delete(sj);
			return "redirect:/recruitment/detail/" + idRe;
		}

		sService.delete(sj);
		redirectAttributes.addFlashAttribute("success",
				"Đã xóa công việc " + sj.getRecruitment().getTitle() + " thành công");

		return "redirect:/save-job/get-list";
	}

}
