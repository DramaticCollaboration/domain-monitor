package com.niulbird.domain.monitor;

import java.io.IOException;
import java.net.Proxy;

import org.apache.commons.net.whois.WhoisClient;

import com.niulbird.domain.Domain;

public class WhoisMonitor extends BaseMonitor {
	private WhoisClient whoisClient;
	private Proxy proxy;
	
	public WhoisMonitor () {
		whoisClient = new WhoisClient();
	}
	
	public WhoisMonitor (Proxy proxy) {
		whoisClient = new WhoisClient();
		this.proxy = proxy;
	}
	
	public void init() throws IOException {
		if (proxy != null) {
			whoisClient.setProxy(proxy);
		}
	    whoisClient.connect(WhoisClient.DEFAULT_HOST);
	}
	
	public void disconnect() throws IOException {
	    whoisClient.disconnect();
	}
	
	public Domain query(String name) {
		String info = new String();
		Domain domain = new Domain();
		domain.setName(name); 
	    try {
	    	if (!whoisClient.isConnected()) {
	    		init();
	    	}
	      	info = whoisClient.query(name);
	      	logger.trace("Domain Info: " + info);
	      	domain = Domain.parseDomain(info);
	    } catch (IOException e) {
	      logger.error("Error I/O exception: " + e.getMessage(), e);
	    }
	    return domain;
	}
}
