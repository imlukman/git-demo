package com.boot.api;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boot.bean.UserDetailsRequestBean;
import com.boot.service.SampleRetryService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api")
public class Controller {
	private final static String greet="Weclome here";
	
	@Autowired
	private SampleRetryService service;
	private static int count=1;
	
	@GetMapping("/wel")
	public String getWelcomeGreet(@RequestParam(value = "ssn") String ssn) {
		return service.invoke(ssn);
	}
	@PostMapping("/user")
	public String getUserDetails(@Valid @RequestBody UserDetailsRequestBean bean,BindingResult result) {
		
		log.info("data {}", bean.toString());
		return greet;
	}
	@GetMapping("/msg")
	public String getMsg() throws Exception {
		service.retryMethod();
		return "hi";
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String,String> handleMethodError(MethodArgumentNotValidException ex) {
		Map<String,String> errorDetail=new HashMap<>();
		
		errorDetail.put("error", ex.getFieldError().getField());
		errorDetail.put("description", ex.getFieldError().getDefaultMessage());
		errorDetail.put("status", HttpStatus.BAD_REQUEST.toString());
		
		log.error("Exception {}",ex.getMessage());
		
		return errorDetail;
	}
}
