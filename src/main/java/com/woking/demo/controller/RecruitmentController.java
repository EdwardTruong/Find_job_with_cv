package com.woking.demo.controller;

import java.security.Security;
import java.text.ParseException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.woking.demo.config.SecurityUtils;
import com.woking.demo.dto.CvDto;
import com.woking.demo.dto.RecruitmentDto;
import com.woking.demo.dto.RecruitmentResponseFindDto;
import com.woking.demo.dto.UserDto;
import com.woking.demo.entity.Applyjob;
import com.woking.demo.entity.CategoryEntity;
import com.woking.demo.entity.CompanyEntity;
import com.woking.demo.entity.RecruitmentEntity;
import com.woking.demo.entity.SaveJob;
import com.woking.demo.entity.UserEntity;
import com.woking.demo.exception.UserNotFoundException;
import com.woking.demo.mapper.CompanyMapper;
import com.woking.demo.mapper.RecruitmentMapper;
import com.woking.demo.service.ApplyjobService;
import com.woking.demo.service.CategoryService;
import com.woking.demo.service.CompanyService;
import com.woking.demo.service.CvService;
import com.woking.demo.service.RecruitmentService;
import com.woking.demo.service.RoleService;
import com.woking.demo.service.SavejobService;
import com.woking.demo.service.UserService;
import com.woking.demo.utils.ApplicationUtils;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/*
 * 1. The getListPost method use to show all posts of company.
 * 2. The getPostForm method use to show a form for creating a new post. 
 * 3. The createNewRecruitment method using create new a post of company and it have validation. @PostMapping.
 * 		-First i find company for prepare fields have errors.
 * 		-Second i set field createAt of Recruitment entity at the moment with name thisMoment type String when a post was created 
 * 				in class ApplicationUtils - method getCurrentDateJavaUtils.
 * 		-Therd checking the date created is after deadline and if it was right i change String thisMoment become type Date then set it to 
 * 				the field createdAt. If something goes wrong when set createdAt field, it will return from making a new post.
 * 		-And then Category entity add Recruitment entity to count the quantity member need of the recruitment poster.
 * 		-Finally save Recruitment entity into database. 
 * 
 * 
 * 4. The recruitmentDetail method for user role and company role can access. 
 * 		When user role access then all post have same category was showed.
 * 		When the company role accesses  the all user's applied job was show, and of course, they see the user's applied job 
 * 			if they access their own posts.
 *	5. The deleteRecruitment nethod use to delete a company's post. @PostMapping
 *			If the post has any user who has submitted a cv company role can't delete a post.
 *
 *	6. The edit method is page show information of a post. 
 *		-In this method i use HttpSession save Recruitment entity to prepear for update value of field category's numberChooes if the post changed value of field category.
 *		-2 lists name listUserSaveJob and listUserApplied use to save all user save a post and all user applied a post.

 *
 *	7. The update method using update post's infomations.
 *			
 *		-If the post changed field category to another value , the value of field category's numberChoose need to updated too.
 *		
 *	**** IMPORTANT ****
 *		-The tabs input have type="hidden" for list users can not update list related to user's information. 
 *			they replace all list users of this post like a new list and all information incluse cv, list user applied, user save a post can not update.
 *		 THE COLUMNS IN DATABASE HAS BEEN DELETE.
 *		
 *		PROBLEM SOLVING : 
 *			I set 2 recruitment'field (a post) listUserApplyThisJob and listUserSavedThisJob is listUserSaveJob & listUserApplied.
 *	**** IMPORTANT ****
 *
 *		-They are used to prepare updates a post
 *
 *	BONUTE : I create MoneyFormatConstraintValidator for to check only number but i didn't use it. It's use to update more function this project later.
 */

@Controller
@RequestMapping("/recruitment")
public class RecruitmentController {

	@Autowired
	CompanyService companyService;

	@Autowired
	RecruitmentService rService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	RecruitmentMapper recruitmentMapper;

	@Autowired
	CompanyMapper companyMapper;

	@Autowired
	RoleService roleService;

	@Autowired
	UserService userService;

	@Autowired
	ApplicationUtils utils;

	@Autowired
	SavejobService savejobService;

	@Autowired
	ApplyjobService aService;

	@Autowired
	CvService cvService;

	public List<UserEntity> listUserSaveJob = new ArrayList<>();

	public List<UserEntity> listUserApplied = new ArrayList<>();

	public List<CategoryEntity> allCategories = new ArrayList<>();

	private static final Logger logger = LoggerFactory.getLogger(RecruitmentController.class);

	// 1. DOUBLE CHECK DONE !
	@GetMapping("/list-post")
	public String getListPost(@RequestParam(value = "page", defaultValue = "1") Integer pageNo,
			RedirectAttributes redirectAttributes, Model theModel, HttpSession session) {

		UserDto userAuthenticated = userService.getCurrentUserDto();

		theModel.addAttribute("user", userAuthenticated);

		int pageSize = 5;
		Page<RecruitmentEntity> recruimenstDtoPaging = rService.listAllRecruitmentPageable(
				userAuthenticated.getIdCompany(), pageNo,
				pageSize);
		int numberPage = recruimenstDtoPaging.getTotalPages();
		int[] arr = utils.getNumberPagation(numberPage);

		theModel.addAttribute("arr", arr);
		theModel.addAttribute("numberPage", numberPage);
		theModel.addAttribute("recruitmentList", recruimenstDtoPaging);
		theModel.addAttribute("list", recruimenstDtoPaging);

		return "public/load/company-post/post-list";
	}

	// 2. DOUBLE CHECK DONE !
	@GetMapping("/post")
	public String getPostForm(Model theModel, RedirectAttributes redirectAttributes, HttpSession session) {

		UserDto userAuthenticated = userService.getCurrentUserDto();

		CompanyEntity companyEntity = companyService.findCompanyEntityById(userAuthenticated.getIdCompany());
		allCategories = categoryService.loadAll();
		theModel.addAttribute("recruitment", new RecruitmentDto());
		theModel.addAttribute("categories", allCategories);
		theModel.addAttribute("company", companyEntity);

		theModel.addAttribute("user", userAuthenticated);
		return "public/load/company-post/post-job";

	}

	// 3. DOUBLE CHECK DONE !
	@PostMapping("/add")
	public String createNewRecruitment(@Valid @ModelAttribute("recruitment") RecruitmentDto recruitment,
			BindingResult bindingResult, Model theModel, HttpSession session, RedirectAttributes redirectAttributes)
			throws ParseException {

		String username = SecurityUtils.getSessionUsername();

		if (username != null) {
			UserDto userAuthenticated = userService.getCurrentUserDto();
			allCategories = categoryService.loadAll();

			CompanyEntity companyEntity = companyService.findCompanyEntityById(userAuthenticated.getIdCompany());

			theModel.addAttribute("company", companyEntity);

			if (bindingResult.hasErrors()) {
				theModel.addAttribute("categories", allCategories);
				return "public/load/company-post/post-job";
			}

			String thisMoment = utils.getCurrentDateJavaUtils();
			String deadline = utils.getStringDate(recruitment.getDeadline());
			logger.info("Load again before check Check in current date and createdAt : createdAt : " + thisMoment);
			logger.info("Load again before check Check in current date and deadline : deadline : " + deadline);

			if (utils.checkInputDate(thisMoment, deadline)) {
				theModel.addAttribute("categories", allCategories);
				theModel.addAttribute("dateError", true);
				return "public/load/company-post/post-job";
			}

			try {
				recruitment.setCreatedAt(utils.getDateByString(thisMoment));
			} catch (DateTimeException e) {
				logger.info(e.getMessage());
				theModel.addAttribute("categories", allCategories);
				return "public/load/company-post/post-job";
			}
			recruitment.setId(0);
			recruitment.setView(0);
			recruitment.setStatus(true);
			RecruitmentEntity reEntity = recruitmentMapper.toEntity(recruitment);
			CategoryEntity categoryEntity = recruitment.getCategory();
			companyEntity.addRecruitment(reEntity);
			categoryEntity.addRecruitment(reEntity);
			categoryEntity.setNumberChoose(categoryEntity.getNumberChoose() + recruitment.getQuantity());

			rService.save(reEntity);
			redirectAttributes.addFlashAttribute("success", "Đã thêm mới bài đăng.");
		}
		return "redirect:/recruitment/list-post";
	}

	// 4. DOUBLE CHECK DONE !
	@GetMapping("/detail/{id}")
	public String recruitmentDetail(@PathVariable("id") Integer id,
			@RequestParam(value = "page", defaultValue = "1") Integer pageNo, HttpSession session, Model theModel,
			RedirectAttributes redirectAttributes) {

		RecruitmentEntity recruitment = rService.findRecruitmenById(id);
		if (recruitment == null) {
			return "errors/error-404";
		}
		CompanyEntity company = recruitment.getCompanyEntity();

		int pageSize = 5;
		int numberPage = 0;
		int categoryId = recruitment.getCategory().getId();

		theModel.addAttribute("recruitment", recruitment);
		theModel.addAttribute("company", company);
		theModel.addAttribute("idRe", recruitment.getId());
		theModel.addAttribute("company_role", false);

		Page<RecruitmentEntity> listReSameCategoryId = rService.findRecruitmentDtoSameCategoryId(categoryId, pageNo,
				pageSize);
		String total = String.valueOf(listReSameCategoryId.getNumberOfElements());
		String listUserSaveJobSize = String.valueOf(listUserSaveJob.size());
		String listUserAppliedSize = String.valueOf(listUserSaveJob.size());
		logger.info("Size list of listReSameCategoryId :" + total);
		logger.info("Size list of recruitment : listUserSaveJobSize : " + listUserSaveJobSize);
		logger.info("Size list of recruitment : listUserAppliedSize : " + listUserAppliedSize);
		numberPage = listReSameCategoryId.getTotalPages();
		theModel.addAttribute("listRelated", listReSameCategoryId);

		String username = SecurityUtils.getSessionUsername();

		if (username != null) {
			UserDto userAuthenticated = userService.getCurrentUserDto();
			theModel.addAttribute("user", userAuthenticated);

			if (!roleService.isCompany(userAuthenticated)) {

				SaveJob sj = savejobService.findJobSavedByUserIdAndRecruitmentId(userAuthenticated.getId(), id);

				if (sj != null) {
					userAuthenticated.setSaveJobId(sj.getId());
				} else {
					userAuthenticated.setSaveJobId(0);
				}
				session.setAttribute("user", userAuthenticated);
				recruitment.setView(recruitment.getView() + 1);
				rService.update(recruitment);

				List<CvDto> cvs = cvService.findCvByUserId(userAuthenticated.getId());
				theModel.addAttribute("cvs", cvs);
			} else {

				theModel.addAttribute("company_role", true);
			}

			if (recruitment.getCompanyEntity().getId() == userAuthenticated.getIdCompany()) {
				Page<Applyjob> applyPosts = aService.listApplyJobByIdRe(id, pageNo, pageSize);
				theModel.addAttribute("applyPosts", applyPosts);
				theModel.addAttribute("company_role", true);
				numberPage = applyPosts.getTotalPages();
			}

		}
		int[] arr = utils.getNumberPagation(numberPage);
		theModel.addAttribute("arr", arr);
		theModel.addAttribute("numberPage", numberPage);
		return "public/load/company-post/detail-post";

	}

	// 5. DOUBLE CHECK DONE !
	@PostMapping("/delete")
	public String deleteRecruitment(@RequestParam("id") Integer idRe, HttpSession session, Model theModel,
			RedirectAttributes redirectAttributes) {

		String username = SecurityUtils.getSessionUsername();

		UserDto userAuthenticated = new UserDto();

		if (username != null) {
			userAuthenticated = userService.getCurrentUserDto();

		}

		try {
			RecruitmentEntity re = rService.findRecruitmenById(idRe);

			if (userAuthenticated.getIdCompany() != re.getCompanyEntity().getId()) {
				return "errors/error-404";
			}

			if (re.getListUserApplyThisJob().size() > 0) {
				redirectAttributes.addFlashAttribute("error", "Không thể xóa bài đăng.");
				return "redirect:/recruitment/list-post";
			}

			int numberChooseOfCategory = re.getQuantity();
			CategoryEntity categoryEntity = categoryService.findById(re.getCategory().getId());
			int currentNumber = categoryEntity.getNumberChoose() - numberChooseOfCategory;
			categoryEntity.setNumberChoose(currentNumber);
			categoryService.save(categoryEntity);

			rService.delete(re);
			redirectAttributes.addFlashAttribute("success", "Xóa thành công bài đăng.");
			return "redirect:/recruitment/list-post";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("messenger", "Not allow !");
			return "errors/error-404";
		}

	}

	// 6. DOUBLE CHECK DONE !
	@GetMapping("/edit/{idRe}")
	public String edit(@PathVariable("idRe") Integer idRe, HttpSession session, Model theModel,
			RedirectAttributes redirectAttributes) {

		UserDto userAuthenticated = userService.getCurrentUserDto();
		theModel.addAttribute("user", userAuthenticated);

		RecruitmentEntity recruitmentEntity = rService.findRecruitmenById(idRe);
		theModel.addAttribute("recruitment", recruitmentEntity);

		RecruitmentDto recruitmentDto = recruitmentMapper.toDto(recruitmentEntity);
		session.setAttribute("recruitmentOld", recruitmentDto);

		allCategories = categoryService.loadAll();
		theModel.addAttribute("categories", allCategories);

		listUserSaveJob = recruitmentEntity.getListUserApplyThisJob();

		listUserApplied = recruitmentEntity.getListUserApplyThisJob();

		String listUserSaveJobSize = String.valueOf(listUserSaveJob.size());
		String listUserAppliedSize = String.valueOf(listUserSaveJob.size());
		logger.info("Size list of recruitment : " + listUserSaveJobSize);
		logger.info("Size list of recruitment : " + listUserAppliedSize);

		return "public/load/company-post/edit-post";
	}

	// 7. DOUBLE CHECK DONE !
	@PostMapping("/update/{idRe}")
	public String update(@PathVariable("idRe") Integer idRe,
			@Valid @ModelAttribute("recruitment") RecruitmentEntity recruitment, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model theModel, HttpSession session) {

		UserDto userAuthenticated = userService.getCurrentUserDto();

		theModel.addAttribute("user", userAuthenticated);

		if (bindingResult.hasErrors()) {
			theModel.addAttribute("categories", allCategories);
			return "public/load/company-post/edit-post";
		}

		String dateCreated = utils.getStringDate(recruitment.getCreatedAt());
		String deadline = utils.getStringDate(recruitment.getDeadline());
		logger.info(
				"Load again before check in update Check in current date and createdAt : createdAt : " + dateCreated);
		logger.info("Load again before check in update Check in current date and deadline : deadline : " + deadline);

		if (utils.checkInputDate(dateCreated, deadline)) {
			theModel.addAttribute("categories", allCategories);
			theModel.addAttribute("dateError", true);
			theModel.addAttribute("createdAt", utils.changeDateFormatDMY(dateCreated));
			return "public/load/company-post/edit-post";
		}

		RecruitmentDto recruitmentDtoOld = (RecruitmentDto) session.getAttribute("recruitmentOld");

		CategoryEntity categoryEntityOld = recruitmentDtoOld.getCategory();
		CategoryEntity categoryEntityCurrent = recruitment.getCategory();

		/*
		 * Changing a type of recruitment. When we change quantity of recruitment we
		 * need to update total number choose of old category by (-) quantity old and
		 * (+) new quantity to new category this (if-else) is make it.
		 */

		if (categoryEntityCurrent.getId() == categoryEntityOld.getId()) {
			int getDefaulNumberChoose = categoryEntityCurrent.getNumberChoose() - recruitmentDtoOld.getQuantity();
			int numberChoose = getDefaulNumberChoose + recruitment.getQuantity();
			categoryEntityCurrent.setNumberChoose(numberChoose);
		} else {
			int minueNumberChooseInCategoryOld = categoryEntityOld.getNumberChoose() - recruitmentDtoOld.getQuantity();
			categoryEntityOld.setNumberChoose(minueNumberChooseInCategoryOld); // Upodate category number choose

			int updateNunberChooseCategoryChanged = categoryEntityCurrent.getNumberChoose() + recruitment.getQuantity();
			categoryEntityCurrent.setNumberChoose(updateNunberChooseCategoryChanged);

			categoryService.save(categoryEntityOld);// Update category new

			categoryEntityCurrent.addRecruitment(recruitment);

		}
		categoryService.update(categoryEntityCurrent);

		recruitment.setListUserApplyThisJob(listUserApplied);
		recruitment.setListUserSavedThisJob(listUserSaveJob);

		rService.update(recruitment);
		logger.info(recruitment.getListUserApplyThisJob().toString());
		logger.info(recruitment.getListUserSavedThisJob().toString());

		redirectAttributes.addFlashAttribute("success", "Cập nhật thành công bài đăng " + recruitment.getTitle());

		return "redirect:/recruitment/edit/" + idRe;
	}

	// 8. DOUBLE CHECK DONE !
	@GetMapping("/category/{idCate}")
	public String category(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@PathVariable("idCate") Integer idCate, RedirectAttributes redirectAttributes, Model theModel,
			HttpSession session) {

		UserDto userAuthenticated = (UserDto) session.getAttribute("user");
		String username = SecurityUtils.getSessionUsername();
		if (userAuthenticated == null && username != null) {
			userAuthenticated = userService.getCurrentUserDto();
		}

		if (userAuthenticated != null) {
			session.setAttribute("user", userAuthenticated);
			theModel.addAttribute("user", userAuthenticated);

			if (!roleService.isCompany(userAuthenticated)) {
				List<CvDto> cvs = cvService.findCvByUserId(userAuthenticated.getId());
				theModel.addAttribute("cvs", cvs);
			}
		}
		CategoryEntity category = categoryService.findById(idCate);
		int pageSize = 5;

		Page<RecruitmentEntity> listReSameCategoryId = rService.findRecruitmentDtoSameCategoryId(idCate, pageNo,
				pageSize);

		int numberPage = listReSameCategoryId.getTotalPages();
		int[] arr = utils.getNumberPagation(numberPage);

		theModel.addAttribute("category", category);
		theModel.addAttribute("idCategory", idCate);
		theModel.addAttribute("arr", arr);
		theModel.addAttribute("numberPage", numberPage);
		theModel.addAttribute("listRelated", listReSameCategoryId);

		return "public/list-re";

	}

	// 9. DOUBLE CHECK DONE !
	@GetMapping("/search")
	public String searchTittle(@RequestParam(value = "keySearch", required = false) String keySearch, Model theModel,
			HttpSession session, @RequestParam(value = "page", defaultValue = "1") Integer page) {
		int pageSize = 5;

		Page<RecruitmentResponseFindDto> results = rService.findReBySearchingTittle(keySearch, page, pageSize);
		String username = SecurityUtils.getSessionUsername();
		if (username != null) {
			UserDto userAuthenticated = userService.getCurrentUserDto();
			theModel.addAttribute("user", userAuthenticated);
			if (!roleService.isCompany(userAuthenticated)) {
				List<CvDto> cvs = cvService.findCvByUserId(userAuthenticated.getId());
				theModel.addAttribute("cvs", cvs);
			}
		}

		int numberPage = results.getTotalPages();
		int[] arr = utils.getNumberPagation(numberPage);

		theModel.addAttribute("numberPage", numberPage);
		theModel.addAttribute("list", results);
		theModel.addAttribute("arr", arr);
		theModel.addAttribute("keySearch", keySearch);
		session.setAttribute("keySearch", keySearch);
		return "public/load/result-search";
	}

	// 10. DOUBLE CHECK DONE !
	@GetMapping("/searchaddress")
	public String searchAddress(@RequestParam(value = "keySearch", required = false) String keySearch, Model theModel,
			HttpSession session, @RequestParam(value = "page", defaultValue = "1") Integer page) {

		int pageSize = 5;
		Page<RecruitmentResponseFindDto> results = rService.findReBySearchingAddress(keySearch, page, pageSize);

		String username = SecurityUtils.getSessionUsername();
		if (username != null) {
			UserDto userAuthenticated = userService.getCurrentUserDto();
			theModel.addAttribute("user", userAuthenticated);
			if (!roleService.isCompany(userAuthenticated)) {
				List<CvDto> cvs = cvService.findCvByUserId(userAuthenticated.getId());
				theModel.addAttribute("cvs", cvs);
			}
		}
		int numberPage = results.getTotalPages();
		int[] arr = utils.getNumberPagation(numberPage);

		theModel.addAttribute("numberPage", numberPage);
		theModel.addAttribute("list", results);
		theModel.addAttribute("arr", arr);
		theModel.addAttribute("keySearch", keySearch);
		session.setAttribute("keySearch", keySearch);
		return "public/load/result-search-address";
	}

}
