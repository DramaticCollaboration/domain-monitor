package com.niulbird.domain.monitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.niulbird.domain.Domain;

public class MonitorApp {
	private static Logger logger = Logger.getLogger(Domain.class);
	
	public static void main(String[] args) {
		Domain domain = null;
		Proxy proxy = null;
		WhoisMonitor monitor = null;
		Properties props = new Properties();
		
		try {
			props.load(MonitorApp.class.getClassLoader().getResourceAsStream("domain-monitor.properties"));
		} catch (IOException e) {
			logger.error("Cannot load properties file: " + e.getMessage(), e);
		}
		
		if (props.getProperty("conn.proxy").equalsIgnoreCase("true")) {
			SocketAddress addr = new InetSocketAddress(props.getProperty("conn.proxy.host"), Integer.parseInt(props.getProperty("conn.proxy.port")));
			if (props.getProperty("conn.proxy.type").equalsIgnoreCase("socks")) {
				proxy = new Proxy(Proxy.Type.SOCKS, addr);
			} else if (props.getProperty("conn.proxy.type").equalsIgnoreCase("http")) {
				proxy = new Proxy(Proxy.Type.HTTP, addr);
			}
		}
		
		Date today = Calendar.getInstance().getTime();
		for (String name : Arrays.asList(props.getProperty("domain.list").split("\\s*,\\s*"))) {
			monitor = new WhoisMonitor(proxy);
			try {
				monitor.init();
			
				domain = monitor.query(name);
				logger.debug("Domain Info: Name=" + domain.getName() + "|Registrar=" + domain.getRegistrar() + "|Create=" 
						+ domain.getCreateDate() + "|Update=" + domain.getUpdateDate() + "|Expiry=" + domain.getExpiryDate());
			
				int daysRemaining =  Days.daysBetween(new DateTime(today), new DateTime(domain.getExpiryDate())).getDays();
								
				if (ArrayUtils.contains(new int[]{5, 10, 30, 60}, daysRemaining)) {
					logger.debug("HELLO" + daysRemaining);
				}
									
				monitor.disconnect();
			} catch (IOException ioe) {
				logger.error("Error on initialization: " + ioe.getMessage(), ioe);
				return;
			}
		}
	}
}