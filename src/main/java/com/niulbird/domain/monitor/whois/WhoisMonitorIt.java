package com.niulbird.domain.monitor.whois;

import java.io.IOException;
import java.net.Proxy;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.niulbird.domain.monitor.model.Domain;

public class WhoisMonitorIt extends WhoisMonitor {
	private static String DOMAIN_NAME = "Domain:             ";
	private static String REGISTRAR = "Organization:     ";
	private static String CREATE_DATE = "Created:            ";
	private static String UPDATE_DATE = "Last Update:        ";
	private static String EXPIRY_DATE = "Expire Date:        ";
	
	public WhoisMonitorIt() {
		super();
	}
	
	public void init(Proxy proxy, String name) throws IOException {
		if (proxy != null) {
			whoisClient.setProxy(proxy);
		}
		whoisClient.connect("whois.nic.it");
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
