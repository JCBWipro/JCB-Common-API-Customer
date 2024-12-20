package com.wipro.jcb.livelink.app.user.web.dto;

import com.wipro.jcb.livelink.app.user.web.entity.AccountEntity;
import com.wipro.jcb.livelink.app.user.web.entity.TenancyEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This AccountTenancyRelation DTO is used to Handle Account and Tenancy Details response
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountTenancyRelation {
	
	private AccountEntity account;
	private TenancyEntity tenancy;

}
