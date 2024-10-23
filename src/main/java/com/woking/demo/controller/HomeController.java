package com.woking.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.woking.demo.config.SecurityUtils;
import com.woking.demo.dto.ApplyjobDto;
import com.woking.demo.dto.CompanyDto;
import com.woking.demo.dto.CvDto;
import com.woking.demo.dto.RecruitmentDto;
import com.woking.demo.dto.UserDto;
import com.woking.demo.entity.CategoryEntity;
import com.woking.demo.entity.FollowCompany;
import com.woking.demo.mapper.UserMapper;
import com.woking.demo.service.ApplyjobService;
import com.woking.demo.service.CategoryService;
import com.woking.demo.service.CompanyService;
import com.woking.demo.service.CvService;
import com.woking.demo.service.FollowCompanyService;
import com.woking.demo.service.RecruitmentService;
import com.woking.demo.service.RoleService;
import com.woking.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

/*
 * 1. The homePage method is first page for all asscess.
 * 2. The homeLoginPage method using when user login success.
 * 		I setting HttpSession.setAttribute("user", dto) to save dto and then set somes (saveJobId & followId) to increase user experience. 	
 * 
 * 3. The jobs method is show all category. I set Httsession in this method to access home page and another pages dosen't get errors.
 * 			-For now anyone can accsess this page but they can't do anything. 
 * 			-I going to finish this function later.  
 * 3. The detailCompany method use to show a basic infomation of company. 
 * 		When user role login success they can use a button follow company.
 * 		When company role login they just see a basic infomation of company like unlogin.  
 */

@Controller
public class HomeController {

	@Autowired
	UserService userService;

	@Autowired
	CompanyService companyService;

	@Autowired
	RoleService rolesService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	ApplyjobService applyjobService;

	@Autowired
	RecruitmentService recruitmentService;

	@Autowired
	FollowCompanyService flService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	CvService cvService;

	public List<CompanyDto> listCompanyDto = new ArrayList<>();
	List<Object[]>  listTopCompanyDto = new ArrayList<>();
	public List<ApplyjobDto> listApplyjobDto = new ArrayList<>();
	public List<RecruitmentDto> listRecruitmentDto = new ArrayList<>();
	public List<CategoryEntity> categories = new ArrayList<>();
	public List<RecruitmentDto> topRecruitment = new ArrayList<>(); // Trước mắt là đưa lên rồi tính sau
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	
	@GetMapping("/")
	public String homePage(Model theModel) {
		
		String username = SecurityUtils.getSessionUsername();
		if (username != null) {
			return "redirect:/home";
		}
		
		
		categories = categoryService.topCategoryEntitiesByNumberChoose();
		listApplyjobDto = applyjobService.loadAllToDto();
		listTopCompanyDto  = companyService.loadTopCompanyToDto();
		listCompanyDto = companyService.loadAllCompanyDto();
		listRecruitmentDto = recruitmentService.loadAllToDto();

		String numberTopComnay = String.valueOf(listTopCompanyDto.size());
		logger.info(numberTopComnay);
		theModel.addAttribute("companies", listTopCompanyDto);
		theModel.addAttribute("numberCandidate", listApplyjobDto.size());
		theModel.addAttribute("numberCompany", listCompanyDto.size());
		theModel.addAttribute("numberRecruitment", listRecruitmentDto.size());
		theModel.addAttribute("categories", categories);
		
		topRecruitment = recruitmentService.findTopRecruitment();
		theModel.addAttribute("recruitments", topRecruitment);

		return "public/load/home";
	}


	@GetMapping("/home")
	public String homeLoginPage(Model theModel, HttpSession session, RedirectAttributes redirectAttributes) {

		UserDto userAuthenticated = userService.getCurrentUserDto();
		
		if (userAuthenticated != null) {
			session.setAttribute("userInformation", userAuthenticated);			
		}

		try {

			if (!rolesService.isCompany(userAuthenticated)) {
				List<CvDto> cvs = cvService.findCvByUserId(userAuthenticated.getId());
				theModel.addAttribute("cvs", cvs);
			}

			session.setAttribute("user", userAuthenticated);
			theModel.addAttribute("user", userAuthenticated);

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messenger", e.getMessage());
			return "errors/error-404";
		}
		categories = categoryService.topCategoryEntitiesByNumberChoose();
		listApplyjobDto = applyjobService.loadAllToDto();
		listCompanyDto = companyService.loadAllCompanyDto();
		listRecruitmentDto = recruitmentService.loadAllToDto();
		listTopCompanyDto  = companyService.loadTopCompanyToDto();
		
		theModel.addAttribute("companies", listTopCompanyDto);
		theModel.addAttribute("numberCandidate", listApplyjobDto.size());
		theModel.addAttribute("numberCompany", listCompanyDto.size());
		theModel.addAttribute("numberRecruitment", listRecruitmentDto.size());
		theModel.addAttribute("categories", categories);

		topRecruitment = recruitmentService.findTopRecruitment();
		theModel.addAttribute("recruitments", topRecruitment);

		return "public/load/home";
	}

	@GetMapping("/jobs")
	public String jobs(Model theModel, HttpSession session, String param) {
		
		String username = SecurityUtils.getSessionUsername();
		
		if(username != null) {
			UserDto userAuthenticated = userService.getCurrentUserDto();
			session.setAttribute("user", userAuthenticated);
			theModel.addAttribute("user", userAuthenticated);
		}
	
		return "public/load/listJob";
	}


	@GetMapping("/detail-company/{id}") // Của user vào xem để apply job . Sẽ sửa sau.
	public String detailCompany(@PathVariable("id") Integer idCompany, Model theModel, HttpSession session) {
		CompanyDto companyDto = companyService.findCompanyDtoByIdToGetDetail(idCompany);
		theModel.addAttribute("company", companyDto);

		String name = SecurityUtils.getSessionUsername();

		if (name != null) {
			UserDto userAuthenticated = userService.getCurrentUserDto();
			FollowCompany fl = flService.findFollowByUserIdAndCompanyId(userAuthenticated.getId(), companyDto.id());
			if (fl != null) {
				userAuthenticated.setFollowId(fl.getId());
			} else {
				userAuthenticated.setFollowId(0);
			}
			theModel.addAttribute("user", userAuthenticated);
			session.setAttribute("user", userAuthenticated);
		}

		return "public/load/user/detail-company";
	}
}