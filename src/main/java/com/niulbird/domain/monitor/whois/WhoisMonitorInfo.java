package com.niulbird.domain.monitor.whois;

import java.io.IOException;
import java.net.Proxy;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.niulbird.domain.Domain;

public class WhoisMonitorInfo extends WhoisMonitor {
	private static String DOMAIN_NAME = "Domain Name: ";
	private static String REGISTRAR = "Registrar: ";
	private static String CREATE_DATE = "Creation Date: ";
	private static String UPDATE_DATE = "Updated Date: ";
	private static String EXPIRY_DATE = "Registry Expiry Date: ";
	
	public WhoisMonitorInfo() {
		super();
	}
	
	public void init(Proxy proxy, String name) throws IOException {
		if (proxy != null) {
			whoisClient.setProxy(proxy);
		}
		whoisClient.connect("whois.afilias.info");
	}

	@Override
	public Domain parseDomain(String info) {
		Domain domain = new Domain();
		
		domain.setName(getValue(info, DOMAIN_NAME));
		domain.setRegistrar(getValue(info, REGISTRAR));
		domain.setCreateDate(stringToDate(getValue(info, CREATE_DATE), "yyyy-MM-dd"));
		domain.setUpdateDate(stringToDate(getValue(info, UPDATE_DATE), "yyyy-MM-dd"));
		domain.setExpiryDate(stringToDate(getValue(info, EXPIRY_DATE), "yyyy-MM-dd"));
		domain.setExpiryDays(Days.daysBetween(new DateTime(Calendar.getInstance().getTime()), new DateTime(domain.getExpiryDate())).getDays());
		
		return domain;
	}
}
