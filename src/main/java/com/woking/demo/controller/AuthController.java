package com.woking.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.woking.demo.config.SecurityUtils;

import com.woking.demo.dto.UserDto;
import com.woking.demo.mapper.UserMapper;
import com.woking.demo.service.RoleService;
import com.woking.demo.service.UserService;
import com.woking.demo.utils.ApplicationUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/*
 * 1. The formLogin method is show form login custom.
 * 
 * 2. Two methods registerForm (@GetMapping) and saveNewUser (@PostMapping) used to 			
 * 		register new a user and it has validation function in saveNewUser method.
 * 		I use function validation to check input re-password. I will make check re-password 
 * 		by javascrip later.  	
 * 
 * 3. The loginError method show form login when user login unsuccess.
 */

@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UserService uService;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	ApplicationUtils appUtil;

	@Autowired
	RoleService roleService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	UserService userService;

	@GetMapping("/login")
	public String formLogin(Model theModel, HttpSession session) {

		String username = SecurityUtils.getSessionUsername();
		if (username != null) {
			UserDto userAuthenticated = userService.getCurrentUserDto();
			theModel.addAttribute("user", userAuthenticated);
		}

		return "public/login/login";
	}

	@GetMapping("/register")
	public String registerForm(Model theModel, HttpSession session, RedirectAttributes redirect) {

		String username = SecurityUtils.getSessionUsername();
		if (username != null) {
			UserDto userAuthenticated = userService.getCurrentUserDto();
			theModel.addAttribute("user", userAuthenticated);
		} else {

			theModel.addAttribute("user", new UserDto());
		}

		return "public/login/register";
	}

	@PostMapping("/register")
	public String saveNewUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result,
			RedirectAttributes redirect, Model theModel, HttpServletRequest request) {

		if (uService.emailExsit(userDto)) {
			redirect.addFlashAttribute("errorEmail", "Email đã được đăng ký - Chọn email khác hoặc quên mật khẩu.");
			redirect.addFlashAttribute("msg_register_error", true);
			return "redirect:/auth/register";
		}

		if (result.hasErrors()) {
			redirect.addFlashAttribute("msg_register_error", true);
			return "public/login/register";
		}

		if (!userDto.getPassword().equals(userDto.getRePassword())) {
			redirect.addFlashAttribute("errorRePassword", "Nhật lại mật khẩu không đúng.");
			redirect.addFlashAttribute("msg_register_error", true);
			return "redirect:/auth/register";
		}

		try {

			String fullName = uService.saveNewUser(userDto);

			redirect.addFlashAttribute("success", "Xin chào " + fullName + "! Bạn đã đăng ký thành công. Đăng nhập.");
			redirect.addFlashAttribute("msg_register_success", true);

			return "redirect:/auth/login";

		} catch (Exception e) {
			return "errors/null-exception";
		}

	}

	@GetMapping("/login-error")
	public String loginError(RedirectAttributes redirect) {
		redirect.addFlashAttribute("loginError", "Email hoặc Mật khẩu sai !");
		return "redirect:/auth/login";
	}

}
