package com.niulbird.domain.monitor.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;


import com.niulbird.domain.monitor.model.MonitorEmail;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Slf4j
public class MailUtil {

	public boolean sendMail(JavaMailSenderImpl mailSender,
			String[] emails,
			String body,
			MonitorEmail emailConfig) {
		log.info("MailUtil::sendMail(): " + body);
		
		boolean retVal = true;
		MimeMessage message = mailSender.createMimeMessage();
		try {
			message.setFrom(new InternetAddress(emailConfig.getFromEmail(), emailConfig.getFromName()));
			message.setSubject(emailConfig.getSubject());
			message.setSentDate(new Date());
			message.setContent(body, "text/html; charset=UTF-8");
			for (String email : emails) {
				InternetAddress emailAddress = new InternetAddress(email);
				message.addRecipient(Message.RecipientType.TO, emailAddress);
			}
			mailSender.send(message);
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
		}
		
		return retVal;
	}
}
