package com.woking.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponeException {

	private int status;
	private String messenger;
	private Long timeSpamt;
}
