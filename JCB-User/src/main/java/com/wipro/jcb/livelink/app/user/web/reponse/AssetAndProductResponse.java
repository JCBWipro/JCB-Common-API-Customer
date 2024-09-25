package com.wipro.jcb.livelink.app.user.web.reponse;

import java.sql.Timestamp;

public interface AssetAndProductResponse {
	
	String getSerial_Number();
	String getTxnData();
	String getEngine_Number();
	String getProduct_ID();
	String getAsseet_Group_Name();
	Timestamp getoperatingStartTime();
	Timestamp getoperatingEndTime();

}
