package com.niulbird.domain.monitor.whois;

import java.io.IOException;
import java.net.Proxy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.whois.WhoisClient;

import com.niulbird.domain.monitor.model.Domain;

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
		logger.info("Getting info for: " + name);
		
		String info = new String();
		Domain domain = new Domain();
		domain.setName(name); 
	    try {
	    	if (!whoisClient.isConnected()) {
	    		init(proxy, name);
	    	}
	      	info = whoisClient.query(name);
	      	logger.info("Domain Info: " + info);
	      	domain = parseDomain(info);
	      	whoisClient.disconnect();
	    } catch (IOException e) {
	      logger.error("Error I/O exception: " + e.getMessage(), e);
	    }
	    return domain;
	}
	
	protected String getValue(String info, String name) {
		int startIndex = info.indexOf(name);
		if(startIndex < 0)
		{
			return null;
		}
		int endIndex = info.indexOf('\n', startIndex);
		return info.substring(startIndex + name.length(), endIndex).replaceAll("(\\r|\\n)", "");
	}
	
	protected Date stringToDate(String input, String format) {
		try {
			return DateUtils.parseDate(input, format);
		} catch (ParseException | NullPointerException e) {
			logger.error("Error parsing date: " + e.getMessage());
		}
		return null;
	}

	protected Date stringToDate(String input, String[] format) {
		try {
			return DateUtils.parseDate(input, format);
		} catch (ParseException | NullPointerException e) {
			logger.error("Error parsing date: " + e.getMessage());
		}
		return null;
	}
}
