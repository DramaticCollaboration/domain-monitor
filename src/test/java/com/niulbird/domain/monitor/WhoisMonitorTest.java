package com.niulbird.domain.monitor;

import org.junit.Test;

import com.niulbird.domain.Domain;
import com.niulbird.domain.whois.WhoisMonitor;
import com.niulbird.domain.whois.WhoisMonitorFactory;

import java.io.IOException;

import org.junit.Assert;

public class WhoisMonitorTest extends BaseTestCase { 

	@Test
	public void queryDefault() {
		String name = "adility.com";
		Domain domain = getDomain(name);
		Assert.assertNotNull(domain.getExpiryDate());
	}
	
	@Test
	public void queryFr() {
		String name = "adility.fr";
		Domain domain = getDomain(name);
		Assert.assertNotNull(domain.getExpiryDate());
	}
	
	@Test
	public void queryInfo() {
		String name = "zreward.info";
		Domain domain = getDomain(name);
		Assert.assertNotNull(domain.getExpiryDate());
	}
	
	@Test
	public void queryIt() {
		String name = "adility.it";
		Domain domain = getDomain(name);
		Assert.assertNotNull(domain.getExpiryDate());
	}
	
	@Test
	public void queryOrg() {
		String name = "zreward.org";
		Domain domain = getDomain(name);
		Assert.assertNotNull(domain.getExpiryDate());
	}
	
	private Domain getDomain(String name) {
		Domain domain = null;
		WhoisMonitor monitor = WhoisMonitorFactory.getWhoisMonitor(null, name);
		try {
			monitor.init(null, name);
			domain = monitor.query(name);
			logger.debug("Domain Info: Name=" + domain.getName() + "|Registrar=" + domain.getRegistrar() + "|Create=" 
			+ domain.getCreateDate() + "|Update=" + domain.getUpdateDate() + "|Expiry=" + domain.getExpiryDate());
		} catch (IOException ioe) {
			logger.error("Error calling Whois: " + ioe.getMessage(), ioe);
		}
		return domain;
	}
}