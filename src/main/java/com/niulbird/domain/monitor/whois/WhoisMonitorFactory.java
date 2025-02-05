package com.niulbird.domain.monitor.whois;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;

public class WhoisMonitorFactory {

	public static WhoisMonitor getWhoisMonitor(String domainName, List<String> whoisOverrideVerisign) {
		if (whoisOverrideVerisign.contains(domainName)) {
			return new WhoisMonitorVerisign();
		}
		
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