package com.example.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorsController implements ErrorController {

	@RequestMapping("/access_denied")
	public String accessDenied() {
		return "errors/error-404";
	}

	@GetMapping("/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "errors/error-404";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "errors/null-exception";
			}
		}
		return "errors/error-404";
	}

	@GetMapping("/error404")
	public String error() {
		return "errors/error-404";
	}

	@GetMapping("/exception")
	public String exception() {
		return "errors/exception";
	}

	@GetMapping("/illegalArgument_exception")
	public String IllegalArgument() {
		return "errors/illegalArgument-exception";
	}

	@GetMapping("/null_Exception")
	public String nullException() {
		return "errors/null-exception";
	}
}
