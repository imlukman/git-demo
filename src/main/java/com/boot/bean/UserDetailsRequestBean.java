package com.boot.bean;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


import com.boot.enums.ContactType;
import com.boot.validation.Conditional;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lukman
 * 
 */
@Builder
@Getter
@Setter
@ToString
@Conditional(selected="contactType",values= {"EMAIL"},required= {"emailId","emailProvider"})
@Conditional(selected="contactType",values= {"MOBILE"},required= {"contactNo","country"})
public class UserDetailsRequestBean {
	
	@NotNull
	private ContactType contactType;
	@Email
	private String emailId;
	private String emailProvider;
	@Digits(integer=10, fraction = 0)
	private long contactNo;
	private String country;
}

