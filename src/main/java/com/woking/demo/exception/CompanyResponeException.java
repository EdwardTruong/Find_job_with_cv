package com.woking.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanyResponeException {

	private int status;
	private String messenger;
	private Long timeSpamt;
}
