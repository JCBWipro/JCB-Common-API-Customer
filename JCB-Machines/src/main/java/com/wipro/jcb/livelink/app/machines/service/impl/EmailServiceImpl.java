package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.config.AWSConfig;
import com.wipro.jcb.livelink.app.machines.dto.EmailTemplate;
import com.wipro.jcb.livelink.app.machines.dto.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.machines.entity.ServiceCallRequest;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.EmailService;
import jakarta.activation.DataHandler;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementation of the EmailService interface for sending emails.
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    @Value("${email.feedback.enable}")
    private boolean feedbackEnable;
    @Value("${user.feedback.mail.receiver}")
    private String receiverId;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AWSConfig awsConfig;
    @Autowired
    Utilities utilities;
    /*@Autowired(required = true)
    SpringTemplateEngine templateEngine;*/


    /**
     *  Sends a feedback email if feedback is enabled.
     * @param feedbackMsg
     * @param userName
     */
   @Override
   public void sendFeedbackEmail(final String feedbackMsg, final String userName) throws ProcessCustomError, IOException {
       if (feedbackEnable) {
           EmailTemplate emailTemplate = new EmailTemplate();
           emailTemplate.setTo(receiverId);
           emailTemplate.setSubject("Feedback from user: " + userName);
           emailTemplate.setBody(createFeedbackEmailBody(userName, feedbackMsg)); // Use helper method

           sendEmail(emailTemplate);
       }
   }

    @Override
    public void sendServiceCallRequestEmail(ServiceCallRequest requestParam,String concernvalues,List<InputStream> imageFile,List<String> fileName,String userName) throws ProcessCustomError {
        log.info("Start : service call request email {}", requestParam.getVin());
        try {
            EmailTemplate emailTemplate = new EmailTemplate();
            emailTemplate.setTo(receiverId);
            emailTemplate.setSubject("Service call request via LiveLink APP for the VIN "+requestParam.getVin());
            emailTemplate.setBody(createServiceCallRequestEmailBody(requestParam, concernvalues,imageFile,fileName,userName)); // Use helper method

            sendEmail(emailTemplate);
        }catch (Exception e){
            log.error("service call request email failed Exception : {}", e.getMessage());
            e.printStackTrace();
            log.error(e.getMessage(), e);
            throw new ProcessCustomError("Issue faced while sending email is ", e.getMessage(),
                    HttpStatus.EXPECTATION_FAILED);

        }

    }

    private String createServiceCallRequestEmailBody(ServiceCallRequest requestParam, String concernvalues, List<InputStream> imageFile, List<String> fileName, String userName) {
       try {
          String customerName=requestParam.getCustomerName()!=null ? requestParam.getCustomerName() : "-";
          String contactName= requestParam.getContactName()!=null ? requestParam.getContactName() : "-";
          String  customerPhone=requestParam.getCustomerPhone()!=null ? requestParam.getCustomerPhone() : "-";
          String customerAlternativePhone=requestParam.getCustomerAlternativePhone()!=null ? requestParam.getCustomerAlternativePhone() : "-";
          String machineNo=requestParam.getVin()!=null ? requestParam.getVin() : "-";
          String machineHmr=requestParam.getMachineHmr()!=null ? requestParam.getMachineHmr() : "-";
          String serviceDealerName=requestParam.getServiceDealerName()!=null ? requestParam.getServiceDealerName() : "-";
          String model= requestParam.getModel()!=null ? requestParam.getModel() : "-";
          String location=requestParam.getMachineLocation()!=null ? requestParam.getMachineLocation() : "-";
          String warrantyStatus=requestParam.getWarrantyStatus()!=null ? requestParam.getWarrantyStatus() : "-";
          String contractStatus=requestParam.getContractStatus()!=null ? requestParam.getContractStatus() : "-";
          String machineStatus=requestParam.getMachineStatus()!=null ? requestParam.getMachineStatus() : "-";
          String concern=concernvalues!=null&& !concernvalues.isEmpty() ? concernvalues : "-";
          Resource resource = new ClassPathResource("templates/servicerequestcalltemplate.html");
          InputStream inputStream = resource.getInputStream();
          String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
          String emailBody =htmlTemplate.replace("{{customerName}}", customerName)
                       .replace("{{contactName}}", contactName)
                       .replace("{{customerPhone}}", customerPhone)
                       .replace("{{customerAlternativePhone}}", customerAlternativePhone)
                       .replace("{{machineNo}}", machineNo)
                       .replace("{{machineHmr}}", machineHmr)
                       .replace("{{serviceDealerName}}", serviceDealerName)
                       .replace("{{model}}", model)
                       .replace("{{location}}", location)
                       .replace("{{warrantyStatus}}", warrantyStatus)
                       .replace("{{contractStatus}}", contractStatus)
                       .replace("{{machineStatus}}", machineStatus)
                       .replace("{{concern}}", concern);

           Multipart multipart = new MimeMultipart();

           /*Context context=new Context();
           MimeBodyPart bodyText = new MimeBodyPart();
           bodyText.setText("Hi Team,\n\nThere is a new service call request via LiveLink APP for the machine, please log a ticket in the system & confirm back to the customer.\n\nThe customer has shared the following details");

           MimeBodyPart content = new MimeBodyPart();
           String healthStatusTemplate = templateEngine.process("ServiceCallRequest",  context);
           content.setText(healthStatusTemplate, String.valueOf(StandardCharsets.UTF_8), "html");
           multipart.addBodyPart(content);*/


           if(imageFile!=null)
           {
               try {

                   int j=1;
                   for(int i =0;i<imageFile.size();i++) {

                       MimeBodyPart messageBodyPart = new MimeBodyPart();
                       ByteArrayDataSource source = new ByteArrayDataSource(imageFile.get(i), "image/png");
                       messageBodyPart.setDataHandler(new DataHandler(source));
                       messageBodyPart.setFileName(fileName.get(i));
                       multipart.addBodyPart(messageBodyPart);
                       log.info("Image attachment");
                       j++;
                   }


               }catch (Exception e) {
                   // TODO: handle exception
                   log.error("Exception occured at image attachment "+e.getMessage());
                   e.printStackTrace();
               }

           }else {
               log.error("Image file is Empty in email content");
           }

           return emailBody;





        /*   Transport transport = null;
           try {
               log.info("Start : SMTP configuration");
               final Date yesterdate = utilities.getDate(utilities.getStartDate(1));
               final Date date = utilities.getStartDateTimeInDateFormat(1);

               final MimeMessage msg = new MimeMessage(session);
               msg.setFrom(new InternetAddress(senderId, senderName));
               log.info("User Name : "+userName);
               if(userName!=null && userName.equals("test545567")) {
                   msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(serviceCallRequestMailId));
               }else {
                   msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(customercaremailId));

               }
               msg.setSubject("Service call request via LiveLink APP for the VIN "+requestParam.getVin());
               log.info("Done : SMTP configuration");
               log.info("Context Template Started");
               Context context = new Context();


               log.info("Context Template Ended");

               Multipart multipart = new MimeMultipart();


               MimeBodyPart bodyText = new MimeBodyPart();
               bodyText.setText("Hi Team,\n\nThere is a new service call request via LiveLink APP for the machine, please log a ticket in the system & confirm back to the customer.\n\nThe customer has shared the following details");
               //multipart.addBodyPart(bodyText);

               MimeBodyPart content = new MimeBodyPart();
               String healthStatusTemplate = templateEngine.process("ServiceCallRequest", context);
               //logger.info("healthStatusTemplate "+healthStatusTemplate);
               content.setText(healthStatusTemplate, "UTF-8", "html");
               multipart.addBodyPart(content);


               if(imageFile!=null)
               {
                   try {

                       int j=1;
                       for(int i =0;i<imageFile.size();i++) {

                           MimeBodyPart messageBodyPart = new MimeBodyPart();
                           ByteArrayDataSource source = new ByteArrayDataSource(imageFile.get(i), "image/png");
                           messageBodyPart.setDataHandler(new DataHandler(source));
                           messageBodyPart.setFileName(fileName.get(i));
                           multipart.addBodyPart(messageBodyPart);
                           log.info("Image attachment");
                           j++;
                       }


                   }catch (Exception e) {
                       // TODO: handle exception
                       log.error("Exception occured at image attachment {}", e.getMessage());
                       e.printStackTrace();
                   }

               }else {
                   log.error("Image file is Empty in email content");
               }


               MimeBodyPart text = new MimeBodyPart();
               text.setText("Thank You.");
               //multipart.addBodyPart(text);

               msg.setContent(multipart);

               //msg.setContent(mp);
               log.info("Before sent email");
               transport = session.getTransport();
               transport.connect(host, smtpUserName, smtpPassword);
               log.info("Host :"+host);
               transport.sendMessage(msg, msg.getAllRecipients());
               log.info("After sent email");
               log.info("Send service call request email");

           } catch (MessagingException e) {
               log.error("service call email failed MessagingException : {}", e.getMessage());
               log.error(e.getMessage(), e);
               e.printStackTrace();
               throw new ProcessCustomError("Issue faced while generating messages is ", e.getMessage(),
                       HttpStatus.EXPECTATION_FAILED);
           } catch (Exception e) {
               log.error("service call request email failed Exception : {}", e.getMessage());
               e.printStackTrace();
               log.error(e.getMessage(), e);
               throw new ProcessCustomError("Issue faced while sending email is ", e.getMessage(),
                       HttpStatus.EXPECTATION_FAILED);
           } finally {
               // Close and terminate the connection.
               try {
                   if (null != transport) {
                       transport.close();
                   }
               } catch (Exception e) {
                   log.error("Issue faced while sending email Final Exception : ", e.getMessage());
                   e.printStackTrace();
               }
           }*/

       }catch (IOException e){
           log.error("Error reading feedback email template: {}", e.getMessage(), e);
           return "Error loading feedback email template.";

       } /*catch (MessagingException e) {
           throw new RuntimeException(e);
       }*/

    }

    /**
     * Creates the body of the feedback email by replacing placeholders in the HTML template.
     */
    private String createFeedbackEmailBody(String userName, String feedbackMsg) {
        try {
            Resource resource = new ClassPathResource("templates/feedback-email-template.html");
            InputStream inputStream = resource.getInputStream();
            String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String emailBody = htmlTemplate.replace("{{userName}}", userName)
                    .replace("{{feedbackMsg}}", feedbackMsg);
            return emailBody;

        } catch (IOException e) {
            log.error("Error reading feedback email template: {}", e.getMessage(), e);
            return "Error loading feedback email template.";
        }
    }

    /**
     * Sends an email using the provided EmailTemplate.
     * @param emailTemplate
     */
    public void sendEmail(EmailTemplate emailTemplate) {
        List<MsgResponseTemplate> responseTemplates = new ArrayList<>();
        String recipient = emailTemplate.getTo();

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipient);
            helper.setFrom(awsConfig.getFromEmail());
            helper.setSubject(emailTemplate.getSubject());
            helper.setText(emailTemplate.getBody(), true);

            javaMailSender.send(message);
            log.info("Email sent successfully to {}", recipient);
            responseTemplates.add(new MsgResponseTemplate("Email sent successfully to " + recipient, true));

        } catch (Exception e) {
            log.error("Error sending email to {}: {}", recipient, e.getMessage());
            responseTemplates.add(new MsgResponseTemplate("Error sending email to " + recipient + ": " + e.getMessage(), false));
        }
    }
}
