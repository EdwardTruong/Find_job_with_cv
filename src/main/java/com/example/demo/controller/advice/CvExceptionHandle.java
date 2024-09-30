package com.example.demo.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.exception.CvNotFoundException;
import com.example.demo.exception.CvResponeException;

@ControllerAdvice
public class CvExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<CvResponeException> notFound(CvNotFoundException exc){
		
		CvResponeException e = new CvResponeException(
				HttpStatus.NOT_FOUND.value(),
				exc.getMessage(), System.currentTimeMillis());
		
		return new ResponseEntity<>(e,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<CvResponeException> badRequest(RuntimeException exc){
		
		CvResponeException e = new CvResponeException(
				HttpStatus.BAD_REQUEST.value(),
				exc.getMessage(), System.currentTimeMillis());
		
		return new ResponseEntity<>(e,HttpStatus.BAD_REQUEST);
	}
}
