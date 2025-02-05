package com.niulbird.domain.monitor.whois;

import java.io.IOException;
import java.net.Proxy;
import java.util.Calendar;

import org.apache.commons.net.whois.WhoisClient;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.niulbird.domain.monitor.model.Domain;

public class WhoisMonitorDefault extends WhoisMonitor {
	private static String DOMAIN_NAME = "Domain Name: ";
	private static String REGISTRAR = "Registrar: ";
	private static String CREATE_DATE = "Creation Date: ";
	private static String UPDATE_DATE = "Updated Date: ";
	private static String EXPIRY_DATE = "Expiration Date: ";
	
	public WhoisMonitorDefault() {
		super();
	}
	
	public void init(Proxy proxy, String name) throws IOException {
		if (proxy != null) {
			whoisClient.setProxy(proxy);
		}
		whoisClient.connect(WhoisClient.DEFAULT_HOST);
	}

	@Override
	public Domain parseDomain(String info) {
		Domain domain = new Domain();
		
		domain.setName(getValue(info, DOMAIN_NAME));
		domain.setRegistrar(getValue(info, REGISTRAR));
		domain.setCreateDate(stringToDate(getValue(info, CREATE_DATE), new String[]{"dd-MMM-yyyy", "yyyy-MM-dd'T'HH:mm:ssX"}));
		domain.setUpdateDate(stringToDate(getValue(info, UPDATE_DATE), new String[]{"dd-MMM-yyyy", "yyyy-MM-dd'T'HH:mm:ssX"}));
		domain.setExpiryDate(stringToDate(getValue(info, EXPIRY_DATE), new String[]{"dd-MMM-yyyy", "yyyy-MM-dd'T'HH:mm:ssX"}));
		domain.setExpiryDays(Days.daysBetween(new DateTime(Calendar.getInstance().getTime()), new DateTime(domain.getExpiryDate())).getDays());
		
		return domain;
	}
}
