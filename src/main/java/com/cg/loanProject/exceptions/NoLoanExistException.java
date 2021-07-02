package com.cg.loanProject.exceptions;

public class NoLoanExistException extends RuntimeException{
	
	public NoLoanExistException(String str) {
		super(str);
	}

}
