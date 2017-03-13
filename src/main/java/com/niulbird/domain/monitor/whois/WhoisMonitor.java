package com.niulbird.domain.monitor.whois;

import java.io.IOException;
import java.net.Proxy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.whois.WhoisClient;

import com.niulbird.domain.Domain;

public abstract class WhoisMonitor implements WhoisMonitorIF {
	protected final Log logger = LogFactory.getLog(getClass());
	
	protected WhoisClient whoisClient;
	protected Proxy proxy;
	
	public WhoisMonitor () {
		whoisClient = new WhoisClient();
	}
	
	public void disconnect() throws IOException {
	    whoisClient.disconnect();
	}
	
	public Domain query(String name) {
		logger.debug("Getting info for: " + name);
		
		String info = new String();
		Domain domain = new Domain();
		domain.setName(name); 
	    try {
	    	if (!whoisClient.isConnected()) {
	    		init(proxy, name);
	    	}
	      	info = whoisClient.query(name);
	      	logger.trace("Domain Info: " + info);
	      	domain = parseDomain(info);
	      	whoisClient.disconnect();
	    } catch (IOException e) {
	      logger.error("Error I/O exception: " + e.getMessage(), e);
	    }
	    return domain;
	}
	
	protected String getValue(String info, String name) {
		int startIndex = info.indexOf(name);
		int endIndex = info.indexOf('\n', startIndex);
		return info.substring(startIndex + name.length(), endIndex).replaceAll("(\\r|\\n)", "");
	}
	
	protected Date stringToDate(String input, String format) {
		Date date = null;
		DateFormat formatter = new SimpleDateFormat(format);
		try {
			date = (Date)formatter.parse(input);
		} catch (ParseException e) {
			logger.error("Error parsing date: " + e.getMessage(), e);
		}
		return date;
	}
}
