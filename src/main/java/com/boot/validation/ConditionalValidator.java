package com.boot.validation;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionalValidator implements ConstraintValidator<Conditional, Object>{

	private String selected;
	private String[] required;
	private String message;
	private String[] values;
	
	@Override
	public void initialize(Conditional constraintAnnotation) {
		selected=constraintAnnotation.selected();
		required=constraintAnnotation.required();
		message=constraintAnnotation.message();
		values=constraintAnnotation.values();

	}
	@Override
	public boolean isValid(Object objectToValidate, ConstraintValidatorContext context) {
		boolean valid=true;
		
		try {
			Object checkedValue=BeanUtils.getProperty(objectToValidate, selected);
			if(Arrays.asList(values).contains(checkedValue)) {
				for(String prop:required) {
					Object 	requiredValue=BeanUtils.getProperty(objectToValidate, prop);
					valid=requiredValue!=null && !ObjectUtils.isEmpty(requiredValue);
					
					log.debug("value :{}", requiredValue);
					if(!valid) {
						context.disableDefaultConstraintViolation();
						context.buildConstraintViolationWithTemplate(message).addPropertyNode(prop).addConstraintViolation();
					}
						
				}
			}
			
		}catch(Exception e) {
			log.error("Exception occured at {}, exception:{}",objectToValidate.getClass().getName(),e);
			return false;
		}
		
		return valid;
	}

}