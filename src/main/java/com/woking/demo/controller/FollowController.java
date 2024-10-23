package com.woking.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.woking.demo.config.SecurityUtils;
import com.woking.demo.dto.UserDto;
import com.woking.demo.entity.CompanyEntity;
import com.woking.demo.entity.FollowCompany;
import com.woking.demo.entity.UserEntity;
import com.woking.demo.service.CompanyService;
import com.woking.demo.service.FollowCompanyService;
import com.woking.demo.service.UserService;
import com.woking.demo.utils.ApplicationUtils;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*
 * 1. The listFollowed method show a list of companies the user has followed and that uses pagination.
 * 2. The followCompany method use to follow a company. @PostMapping
 * 3. The unfollowCompany method is delete entity FollowCompany entity. @PostMapping 
 * 		.I did use checking current user by HttpSession. I use a field User().getId() into FollowCompany to check before delete it.
 * 			
 */

@Controller
@RequestMapping("/follow")
public class FollowController {

	@Autowired
	FollowCompanyService flService;

	@Autowired
	UserService userService;

	@Autowired
	CompanyService companyService;

	@Autowired
	ApplicationUtils appUtil;

	@GetMapping("/get-list")
	public String listFollowed(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			RedirectAttributes redirectAttributes, Model theModel, HttpSession session) {

		UserDto userAuthenticated = userService.getCurrentUserDto();
		theModel.addAttribute("user", userAuthenticated);
		session.setAttribute("user", userAuthenticated);

		int pageSize = 3;
		Page<FollowCompany> followListPagination = flService.findAllFollowOfUser(userAuthenticated.getId(), pageNo, pageSize);

		int numberPage = followListPagination.getTotalPages();
		int[] arr = appUtil.getNumberPagation(numberPage);

		theModel.addAttribute("arr", arr);
		theModel.addAttribute("numberPage", numberPage);
		theModel.addAttribute("followList", followListPagination);
		return "public/load/user/list-follow-company";
	}

	
	@PostMapping("/follow-company/{idCompany}")
	public ResponseEntity<String> followCompany(@RequestParam("idCompany") Integer idCompany, HttpSession session) {

		String username = SecurityUtils.getSessionUsername();
		if (username == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("false"); // User not logged in
		}
		UserDto userAuthenticated  = userService.getCurrentUserDto();
		session.setAttribute("user", userAuthenticated);
		try {
			FollowCompany fl = flService.findFollowByUserIdAndCompanyId(userAuthenticated.getId(), idCompany);
			if (fl == null) {
				UserEntity uEntity = userService.findById(userAuthenticated.getId());
				CompanyEntity company = companyService.findCompanyEntityById(idCompany);
				flService.save(uEntity, company);
				return ResponseEntity.ok("true"); // Follow successful
			} else {
				return ResponseEntity.ok("false"); // Follow unsuccessful
			}
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
	}

	//
	@GetMapping("/delete-follow/{id}")
	public String unfollowCompany(@PathVariable("id") Integer id, Model theModel, HttpSession session,
			RedirectAttributes redirectAttributes) {

		UserDto userAuthenticated = userService.getCurrentUserDto();
		
		FollowCompany fCompany = flService.findById(id);
		
		if(userAuthenticated.getId() != fCompany.getUser().getId()) {
			return "errors/error-404";
		}
		
		if (userAuthenticated.getFollowId() == fCompany.getId()) {
			int companyId = fCompany.getCompany().getId();
			flService.delete(fCompany);
			return "redirect:/detail-company/" + companyId;
		}
		if (fCompany != null) {
			flService.delete(fCompany);
			redirectAttributes.addFlashAttribute("success",
					"Đã hủy theo dõi " + fCompany.getCompany().getName() + " thành công");
		}
		return "redirect:/follow/get-list";
	}

}
