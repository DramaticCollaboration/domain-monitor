package com.niulbird.domain.monitor.whois;

public class WhoisMonitorFactory {

	public static WhoisMonitor getWhoisMonitor(String domainName) {
		if (domainName.endsWith(".fr")) {
			return new WhoisMonitorFr();
		} else if (domainName.endsWith(".info")) {
			return new WhoisMonitorInfo();
		} else if (domainName.endsWith(".it")) {
			return new WhoisMonitorIt();
		} else if (domainName.endsWith(".org")) {
			return new WhoisMonitorOrg();
		} else {
			return new WhoisMonitorDefault();
		}
	}
}