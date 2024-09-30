package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CvResponeException {
	private int status;
	private String messenger;
	private Long timeSpamt;
}