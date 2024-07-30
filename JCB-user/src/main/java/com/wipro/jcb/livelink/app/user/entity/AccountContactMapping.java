package com.wipro.jcb.livelink.app.user.entity;

import com.wipro.jcb.livelink.app.user.dto.AccountContactRelation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_contact")
@IdClass(AccountContactRelation.class)
public class AccountContactMapping {
	
	@Id
	@OneToOne(targetEntity = AccountEntity.class)
    @JoinColumn(name = "Account_ID")
	private AccountEntity account;
	
	@Id
	@OneToOne(targetEntity = ContactEntity.class)
    @JoinColumn(name = "Contact_ID")
	private ContactEntity contact;

}
