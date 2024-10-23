package com.wipro.jcb.livelink.app.reports.service;

import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.entity.MachineFeedParserData;
import com.wipro.jcb.livelink.app.reports.exception.ProcessCustomError;

public interface EmailService {
	
	public void sendPremiumRequestEmail(String vin, String userName,Machine machine,MachineFeedParserData machineFeedParserData, String email) throws ProcessCustomError;

}
