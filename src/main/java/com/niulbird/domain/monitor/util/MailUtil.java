package com.niulbird.domain.monitor.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailUtil {
	private final Logger log = Logger.getLogger(MailUtil.class);
	
	public boolean sendMail(JavaMailSenderImpl mailSender,
			String[] emails,
			String body,
			Environment env) {
		log.info("MailUtil::sendMail(): " + body);
		
		boolean retVal = true;
		MimeMessage message = mailSender.createMimeMessage();
		try {
			message.setFrom(new InternetAddress(env.getProperty("email.fromEmail"), env.getProperty("email.fromName")));
			message.setSubject(env.getProperty("email.subject"));
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
