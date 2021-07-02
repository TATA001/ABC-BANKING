package com.cg.loanProject.exceptions;

public class InvalidUserNameOrPasswordException extends RuntimeException {
	
	public InvalidUserNameOrPasswordException(String str) {
		super(str);
	}

}
