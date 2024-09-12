package com.wipro.jcb.livelink.app.user.web.dto;

import com.wipro.jcb.livelink.app.user.web.entity.AccountEntity;
import com.wipro.jcb.livelink.app.user.web.entity.ContactEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This AccountContactRelation DTO is used to Handle Account and Contact Details response
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountContactRelation {
	
	private AccountEntity account;
	private ContactEntity contact;

}
