package com.techlabs.insurance.globalExpection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.techlabs.insurance.exceptions.AccountNotFoundException;
import com.techlabs.insurance.exceptions.AccountStatusException;
import com.techlabs.insurance.exceptions.AgentNotFoundException;
import com.techlabs.insurance.exceptions.ClaimNotFoundException;
import com.techlabs.insurance.exceptions.IllegalArgumentExceptionCustom;
import com.techlabs.insurance.exceptions.InsufficientBalanceException;
import com.techlabs.insurance.exceptions.InsurancePlanNotFoundException;
import com.techlabs.insurance.exceptions.InsuranceSchemeNotFound;
import com.techlabs.insurance.exceptions.PolicyNotFoundException;
import com.techlabs.insurance.exceptions.UserAPIException;


@ControllerAdvice
public class GlobalExceptionalHandler {
	
	@ExceptionHandler(UserAPIException.class)
	public ResponseEntity<?> handleUserAlreadyExists(UserAPIException e){
		System.out.println(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<?> handleAccountNotFound(AccountNotFoundException e){
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccountStatusException.class)
	public ResponseEntity<?> handleAccountStatusException(AccountStatusException e){
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InsurancePlanNotFoundException.class)
	public ResponseEntity<?> handleInsurancePlanNotFound(InsurancePlanNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IllegalArgumentExceptionCustom.class)
	public ResponseEntity<?> handleInsurancePlanNotFound(IllegalArgumentExceptionCustom e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PolicyNotFoundException.class) 
    public ResponseEntity<?> handlePolicyNotFoundException(PolicyNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(AgentNotFoundException.class)
    public ResponseEntity<?> handleAgentNotFoundException(AgentNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<?> handleInsufficientBalanceException(InsufficientBalanceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ClaimNotFoundException.class)
    public ResponseEntity<?> handleClaimNotFoundeException(ClaimNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InsuranceSchemeNotFound.class)
    public ResponseEntity<?> handleSchemeNotFoundeException(InsuranceSchemeNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
