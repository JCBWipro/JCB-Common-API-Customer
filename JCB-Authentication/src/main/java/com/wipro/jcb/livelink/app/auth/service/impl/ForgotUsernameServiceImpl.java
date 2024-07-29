package com.wipro.jcb.livelink.app.auth.service.impl;

import com.wipro.jcb.livelink.app.auth.model.EmailTemplate;
import com.wipro.jcb.livelink.app.auth.model.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.auth.model.SMSTemplate;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import com.wipro.jcb.livelink.app.auth.service.AWSEmailService;
import com.wipro.jcb.livelink.app.auth.service.ForgotUsernameService;
import com.wipro.jcb.livelink.app.auth.service.UnicelSmsService;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-07-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class ForgotUsernameServiceImpl extends ForgotUsernameService {


    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ForgotUsernameServiceImpl.class);
    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private UnicelSmsService unicelSmsService;

    @Autowired
    private AWSEmailService awsEmailService;

    @Value("${jcb.account.url}")
    private String jcbAccountUrl;

    public MsgResponseTemplate forgotUsername(String mobileNumber, String emailId) {
        try {
            String username;
            String firstName = contactRepo.findFirstNameFromID(emailId);
            if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
                username = contactRepo.findByMobileNumber(mobileNumber);
                if (username == null) {
                    return new MsgResponseTemplate("No user found with this mobile number.", false);
                }
                // Send SMS with username
                SMSTemplate smsTemplate = new SMSTemplate();
                smsTemplate.setTo(Collections.singletonList(mobileNumber));
                String body = "Your login id registered with JCB LiveLink is " + username +
                        " . JCB LiveLink Team.; Dt: -JCB LiveLink";
                smsTemplate.setMsgBody(Collections.singletonList(body));
                unicelSmsService.sendSms(smsTemplate);

                return new MsgResponseTemplate("Username sent to mobile number: " + mobileNumber, true);

            } else if (emailId != null && !emailId.trim().isEmpty()) {
                username = contactRepo.findByEmailId(emailId);
                if (username == null) {
                    return new MsgResponseTemplate("No user found with this email ID.", false);
                }
                // Send email with username
                EmailTemplate emailTemplate = new EmailTemplate();
                emailTemplate.setTo(emailId);
               /* String toList = emailId;
                toList = toList + "," + "lli-support@wipro.com";
                InternetAddress[] emailToList = getAddressList(toList);
                emailTemplate.setTo(Arrays.toString(emailToList));*/
                emailTemplate.setSubject("Your login details registered with JCB LiveLink");
                String body = "<html><body>" +
                        "Hello " + firstName + ",<br><br>\n" +
                        "Please find your user name registered with JCB LiveLink :<br><br>" +
                        "User Name : " + username + "<br><br>" +
                        "Please use this login ID when you log in to LiveLink application next time.<br><br>" +
                        "Application URL : <a href=\"" + jcbAccountUrl + "\">" + jcbAccountUrl + "</a><br><br><br>\n" +
                        "With regards,<br>\n" +
                        "JCB LiveLink Team." +
                        "</body></html>";
                emailTemplate.setBody(body);
                awsEmailService.sendEmail(emailTemplate);


                return new MsgResponseTemplate("Username sent to email ID: " + emailId, true);

            } else {
                return new MsgResponseTemplate("Either mobile number or email ID is required.", false);
            }

        } catch (Exception e) {
            log.error("Error retrieving username: {}", e.getMessage(), e);
            return new MsgResponseTemplate("An unexpected error occurred. Please try again later.", false);
        }
    }

    public InternetAddress[] getAddressList(String toList) {
        List<InternetAddress> emails = new LinkedList<>();
        String[] address = toList.split(",");

        for (String tempAddress : address) {
            tempAddress = tempAddress.trim();
            if (!tempAddress.isEmpty()) {
                try {
                    emails.add(new InternetAddress(tempAddress));
                } catch (AddressException e) {
                    log.error("Exception: {}", e.getMessage());
                }
            }
        }

        return emails.toArray(new InternetAddress[0]);
    }
}
