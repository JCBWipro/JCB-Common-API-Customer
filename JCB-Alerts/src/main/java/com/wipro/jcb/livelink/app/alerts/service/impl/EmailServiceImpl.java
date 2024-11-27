package com.wipro.jcb.livelink.app.alerts.service.impl;

import com.wipro.jcb.livelink.app.alerts.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-11-2024
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${transport.protocol}")
    String protocol;
    @Value("${smtp.starttls.enable}")
    String startTLS;

    @Value("${smtp.auth}")
    String auth;
    @Value("${smtp.port}")
    String port;
    @Value("${smtp.starttls.protocol}")
    String tlsProtocol;

    @Value("${smtp.host}")
    String host;

    @Value("${smtp.username}")
    String smtpUserName;

    @Value("${smtp.password}")
    String smtpPassword;

    @Value("${sender.id}")
    String senderId;

    @Value("${sender.name}")
    String senderName;

    @Value("${user.retrystatus.mail.receiver}")
    String retryStatusMailId;


    @Override
    public void sentRetryMail(String type, String errorMessage, int count, int retryMachineDataCount) {
        log.info("Retry Mail Start");
        Properties props = new Properties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.starttls.enable", startTLS);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.protocols", tlsProtocol);
        props.put("mail.smtp.ssl.trust", host);
        final Session session = Session.getDefaultInstance(props);
        Transport transport = null;
        try {
            transport = session.getTransport();
            transport.connect(host, smtpUserName, smtpPassword);
            final MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderId, senderName));
            msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(retryStatusMailId));
            msg.setSubject("Alert: " + type + " seems to be Retry");

            String retryCount = "";

            if (type.contains("Advanced Report Scheduler")) {
                if (retryMachineDataCount == 1) {
                    retryCount = "1st";
                } else if (retryMachineDataCount == 2) {
                    retryCount = "2nd";
                } else if (retryMachineDataCount == 3) {
                    retryCount = "3rd";
                }
            } else {
                if (retryMachineDataCount == 0) {
                    retryCount = "1st";
                } else if (retryMachineDataCount == 1) {
                    retryCount = "2nd";
                } else if (retryMachineDataCount == 2) {
                    retryCount = "3rd";
                }
            }


            StringBuilder st = new StringBuilder();
            st.append("<html><body><p>Hi Team,<br>").append(type).append(" retrying ").append(retryCount).append(" time in ").append(count).append(" page due to <b>").append(errorMessage).append("<b>");
            log.info("Mail details : type-{},errorMessage{},count{},retryMachineDataCount{}", type, errorMessage, count, retryMachineDataCount);
            st.append("<br><br>Thank You.</p></body></html>");
            msg.setContent(st.toString(), "text/html");
            transport.sendMessage(msg, msg.getAllRecipients());

            log.info("Retry alert mail  {}", new Date());


        } catch (MessagingException e) {
            log.error("Retry not sent due to MessagingException", e);
        }
        catch (Exception e) {
            log.error("Retry not sent due to Exception", e);
        } finally {
            // Close and terminate the connection.
            try {
                if (null != transport) {
                    transport.close();
                }
            } catch (Exception e) {
                log.error("issue faced while sending email  Exception : ", e);
            }
        }

    }
}
