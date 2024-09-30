package com.example.demo.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.exception.CompanyNotFoundException;
import com.example.demo.exception.CompanyResponeException;

@ControllerAdvice
public class CompanyExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<CompanyResponeException> notFound(CompanyNotFoundException exc){
		
		CompanyResponeException e = new CompanyResponeException(
				HttpStatus.NOT_FOUND.value(),
				exc.getMessage(), System.currentTimeMillis());
		
		return new ResponseEntity<>(e,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<CompanyResponeException> badRequest(RuntimeException exc){
		
		CompanyResponeException e = new CompanyResponeException(
				HttpStatus.BAD_REQUEST.value(),
				exc.getMessage(), System.currentTimeMillis());
		
		return new ResponseEntity<>(e,HttpStatus.BAD_REQUEST);
	}
}
