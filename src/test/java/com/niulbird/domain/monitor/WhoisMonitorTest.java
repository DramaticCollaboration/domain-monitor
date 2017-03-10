package com.niulbird.domain.monitor;

import org.junit.Test;

import com.niulbird.domain.Domain;

import java.io.IOException;

import org.junit.Assert;

public class WhoisMonitorTest extends BaseTestCase { 

	@Test
	public void query() {
		Domain domain = null;
		WhoisMonitor monitor = new WhoisMonitor();
		try {
			monitor.init();
			domain = monitor.query("cashtie.com");
			logger.debug("Domain Info: Name=" + domain.getName() + "|Registrar=" + domain.getRegistrar() + "|Create=" 
			+ domain.getCreateDate() + "|Update=" + domain.getUpdateDate() + "|Expiry=" + domain.getExpiryDate());
		} catch (IOException ioe) {
			logger.error("Error calling Whois: " + ioe.getMessage(), ioe);
		}
		Assert.assertNotNull(domain.getExpiryDate());
	}

}
