package com.niulbird.domain.monitor.model;

import java.util.Date;

/**
 * @author nbird
 *
 */
public class Certificate extends BaseModel {
	private String name;
	private String subject;
	private String issuer;
	
	public Certificate(String name, String subject, String issuer, Date createDate, Date expiryDate, int expiryDays) {
		this.name = name;
		this.subject = subject;
		this.issuer = issuer;
		this.createDate = createDate;
		this.expiryDate = expiryDate;
		this.expiryDays = expiryDays;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
}
