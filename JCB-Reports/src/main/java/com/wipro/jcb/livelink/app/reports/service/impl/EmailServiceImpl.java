package com.wipro.jcb.livelink.app.reports.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.reports.config.EmailConfig;
import com.wipro.jcb.livelink.app.reports.dto.EmailTemplate;
import com.wipro.jcb.livelink.app.reports.dto.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.entity.MachineFeedParserData;
import com.wipro.jcb.livelink.app.reports.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.reports.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
	
    @Autowired
    JavaMailSender javaMailSender;
    
    @Autowired
    EmailConfig emailConfig;
	
    /**
     * Implementation of the EmailService interface for sending Email.
     */
	@Override
	public void sendPremiumRequestEmail(String vin, String userName, Machine machine,
			MachineFeedParserData machineFeedParserData, String teamEmail) throws ProcessCustomError {
		log.info("Start : premium call request email " + vin);
		try {
			EmailTemplate emailTemplate = new EmailTemplate();
			emailTemplate.setTo(emailConfig.getReceiverId());
			emailTemplate.setSubject("Premium Request via LiveLink APP for the VIN: " + vin);
			emailTemplate.setBody(createJoinPremiumEmailBody(userName, machine, machineFeedParserData, teamEmail));
			sendEmail(emailTemplate, teamEmail);

		} catch (Exception e) {
			log.error("service call request email failed Exception : {}", e.getMessage());
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw new ProcessCustomError("Issue faced while sending email is ", e.getMessage(),HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	/**
     * Creates the body of the feedback email by replacing placeholders in the HTML template.
     */
	private String createJoinPremiumEmailBody(String userName, Machine machine, MachineFeedParserData machineFeedParserData, String teamEmail) {
		try {
			String customerName = machine.getCustomerName()!=null ? machine.getCustomerName() : "-";
			String customerPhone = (machine.getCustomerNumber()!=null && !machine.getCustomerNumber().isEmpty() && !machine.getCustomerNumber().equals("0")  && machine.getCustomerNumber()!="0" && !machine.getCustomerNumber().equals("NONE")) ? machine.getCustomerNumber() : "-";
			String machineNo = machine.getVin()!=null ? machine.getVin() : "-";
			Double machineHmr;
			if(machineFeedParserData!=null && null != machineFeedParserData.getStatusAsOnTime()) {
				machineHmr = machineFeedParserData.getTotalMachineHours()!=null ? machineFeedParserData.getTotalMachineHours() : 0.0;
			}else {
				machineHmr = machine.getTotalMachineHours()!=null ? machine.getTotalMachineHours() : 0.0;
			}
			String model = machine.getModel()!=null ? machine.getModel() : "-";
			String dealerName = (machine.getDealerName()!=null && !machine.getDealerName().isEmpty()) ? machine.getDealerName() : "-";
            String dealerEmail = (machine.getDealerEmail()!=null && !machine.getDealerEmail().isEmpty()) ? machine.getDealerEmail() : "-";
			String dealerNumber = (machine.getDealerNumber()!=null && !machine.getDealerNumber().isEmpty() && !machine.getDealerNumber().equals("NONE") && !machine.getDealerNumber().equals("0") && machine.getDealerNumber()!="0") ? machine.getDealerNumber() : "-";
			
            Resource resource = new ClassPathResource("templates/join-premium-email-template.html");
            InputStream inputStream = resource.getInputStream();
            String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String emailBody = htmlTemplate.replace("{{customerName}}", customerName)
							        		.replace("{{customerPhone}}", customerPhone)
							        		.replace("{{machineNo}}", machineNo)
							        		.replace("{{machineHmr}}", machineHmr.toString())
							        		.replace("{{model}}", model)
							        		.replace("{{dealerName}}", dealerName)
							        		.replace("{{dealerEmail}}", dealerEmail)
							        		.replace("{{dealerNumber}}", dealerNumber);
            return emailBody;

        } catch (IOException e) {
            log.error("Error reading feedback email template: {}", e.getMessage(), e);
            return "Error loading feedback email template.";
        }
	}

	/**
	 * Sends an email using the provided EmailTemplate.
	 */
	public void sendEmail(EmailTemplate emailTemplate, String teamEmail) {
		List<MsgResponseTemplate> responseTemplates = new ArrayList<>();
		String recipient = emailTemplate.getTo();

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(recipient);
			helper.setFrom(emailConfig.getFromEmail());
			helper.setSubject(emailTemplate.getSubject());
			helper.setText(emailTemplate.getBody(), true);
			helper.addCc(teamEmail);

			javaMailSender.send(message);
			log.info("Email sent successfully to {}", recipient);
			responseTemplates.add(new MsgResponseTemplate("Email sent successfully to " + recipient, true));

		} catch (Exception e) {
			log.error("Error sending email to {}: {}", recipient, e.getMessage());
			responseTemplates.add(new MsgResponseTemplate("Error sending email to " + recipient + ": " + e.getMessage(), false));
		}
	}

}
