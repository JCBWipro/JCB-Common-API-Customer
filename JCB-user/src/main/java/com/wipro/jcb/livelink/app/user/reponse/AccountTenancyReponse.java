package com.wipro.jcb.livelink.app.user.reponse;

/**
 * This AccountTenancyReponse is used to Handle Account and Tenancy Related Details
 */
public interface AccountTenancyReponse {

	 String getAccountId();

	 String getAccountName();

	 String getAccountCode();

	 String getMappingCode();

	 String getTenancyId();

	 String getTenancyName();

}
