package com.niulbird.domain.monitor.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailUtil {
	private final Logger log = Logger.getLogger(MailUtil.class);
	
	public boolean sendMail(JavaMailSenderImpl mailSender,
			String[] emails,
			String body,
			Properties props) {
		log.info("MailUtil::sendMail(): " + body);
		
		boolean retVal = true;
		MimeMessage message = mailSender.createMimeMessage();
		try {
			message.setFrom(new InternetAddress(props.getProperty("email.fromEmail"), props.getProperty("email.fromName")));
			message.setSubject(props.getProperty("email.subject"));
			message.setSentDate(new Date());
			message.setContent(body, "text/html; charset=UTF-8");
			for (String email : emails) {
				InternetAddress emailAddress = new InternetAddress(email);
				message.addRecipient(Message.RecipientType.TO, emailAddress);
			}
			mailSender.send(message);
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retVal;
	}
}
