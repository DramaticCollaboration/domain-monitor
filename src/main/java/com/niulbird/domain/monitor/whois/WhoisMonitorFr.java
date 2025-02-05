package com.niulbird.domain.monitor.whois;

import java.io.IOException;
import java.net.Proxy;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.niulbird.domain.monitor.model.Domain;

public class WhoisMonitorFr extends WhoisMonitor {
	private static String DOMAIN_NAME = "domain:      ";
	private static String REGISTRAR = "registrar:   ";
	private static String CREATE_DATE = "created:     ";
	private static String UPDATE_DATE = "last-update: ";
	private static String EXPIRY_DATE = "Expiry Date: ";
	
	public WhoisMonitorFr() {
		super();
	}
	
	public void init(Proxy proxy, String name) throws IOException {
		if (proxy != null) {
			whoisClient.setProxy(proxy);
		}
		whoisClient.connect("whois.nic.fr");
	}

	@Override
	public Domain parseDomain(String info) {
		Domain domain = new Domain();
		
		domain.setName(getValue(info, DOMAIN_NAME));
		domain.setRegistrar(getValue(info, REGISTRAR));
		domain.setCreateDate(stringToDate(getValue(info, CREATE_DATE), "dd/MM/yyyy"));
		domain.setUpdateDate(stringToDate(getValue(info, UPDATE_DATE), "dd/MM/yyyy"));
		domain.setExpiryDate(stringToDate(getValue(info, EXPIRY_DATE), "dd/MM/yyyy"));
		domain.setExpiryDays(Days.daysBetween(new DateTime(Calendar.getInstance().getTime()), new DateTime(domain.getExpiryDate())).getDays());
		
		return domain;
	}
}
