package com.wipro.jcb.livelink.app.user.reponse;

/**
 * This AccountTenancyReponse is used to Handle Account and Tenancy Related Details
 */
public interface AccountTenancyReponse {

	public String getAccountId();

	public String getAccountName();

	public String getAccountCode();

	public String getMappingCode();

	public String getTenancyId();

	public String getTenancyName();

}
