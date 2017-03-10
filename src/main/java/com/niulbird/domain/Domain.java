package com.niulbird.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


public class Domain {
	private static Logger logger = Logger.getLogger(Domain.class);
	
	private static String DOMAIN_NAME = "Domain Name: ";
	private static String REGISTRAR = "Registrar: ";
	private static String CREATE_DATE = "Creation Date: ";
	private static String UPDATE_DATE = "Updated Date: ";
	private static String EXPIRY_DATE = "Expiration Date: ";
	
	String name;
	String registrar;
	Date createDate;
	Date updateDate;
	Date expiryDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegistrar() {
		return registrar;
	}
	public void setRegistrar(String registrar) {
		this.registrar = registrar;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public static Domain parseDomain(String info) {
		Domain domain = new Domain();
		
		domain.setName(getValue(info, DOMAIN_NAME));
		domain.setRegistrar(getValue(info, REGISTRAR));
		domain.setCreateDate(stringToDate(getValue(info, CREATE_DATE)));
		domain.setUpdateDate(stringToDate(getValue(info, UPDATE_DATE)));
		domain.setExpiryDate(stringToDate(getValue(info, EXPIRY_DATE)));
		
		return domain;
	}
	
	private static String getValue(String info, String name) {
		int startIndex = info.indexOf(name);
		int endIndex = info.indexOf('\n', startIndex);
		return info.substring(startIndex + name.length(), endIndex);
	}
	
	private static Date stringToDate(String input) {
		Date date = null;
		DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			date = (Date)formatter.parse(input);
		} catch (ParseException e) {
			logger.error("Error parsing date: " + e.getMessage(), e);
		}
		return date;
	}
}
