package com.cg.loanProject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cg.loanProject.exceptions.*;

@ControllerAdvice
public class ProjectControllerAdvise {
	
	@ExceptionHandler(InvalidUserNameOrPasswordException.class)
    public final ResponseEntity<String> exceptionHandler(InvalidUserNameOrPasswordException e) 
    {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }	
	
	@ExceptionHandler(AccountAlreadyExistException.class)
	 public final ResponseEntity<String> exceptionHandler(AccountAlreadyExistException e) 
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(AssetValueException.class)
	 public final ResponseEntity<String> exceptionHandler(AssetValueException e) 
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(LoanAmountException.class)
	 public final ResponseEntity<String> exceptionHandler(LoanAmountException e) 
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InsufficientFundsException.class)
	 public final ResponseEntity<String> exceptionHandler(InsufficientFundsException e) 
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(LoanExistException.class)
	 public final ResponseEntity<String> exceptionHandler(LoanExistException e) 
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NoAccountFoundException.class)
	 public final ResponseEntity<String> exceptionHandler(NoAccountFoundException e) 
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NoLoanExistException.class)
	 public final ResponseEntity<String> exceptionHandler(NoLoanExistException e) 
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoDataFoundException.class)
	 public final ResponseEntity<String> exceptionHandler(NoDataFoundException e) 
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

}
