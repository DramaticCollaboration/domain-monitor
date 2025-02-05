//package com.niulbird.domain.monitor;
//
//import com.niulbird.domain.monitor.model.Domain;
//import com.niulbird.domain.monitor.whois.WhoisMonitor;
//import com.niulbird.domain.monitor.whois.WhoisMonitorFactory;
//import org.junit.jupiter.api.Test;
//import org.springframework.util.Assert;
//
//import java.io.IOException;
//import java.util.Properties;
//
//public class WhoisMonitorTest extends BaseTestCase {
//
//	@Test
//	public void queryUnvus() {
//		String name = "unvus.com";
//		Domain domain = getDomain(name);
//		Assert.notNull(domain.getCreateDate(), "Creation Date can not be null");
//	}
//
//	@Test
//	public void queryBasic() {
//		String name = "basicit.co.kr";
//		Domain domain = getDomain(name);
//		Assert.notNull(domain.getCreateDate(), "Creation Date can not be null");
//	}
//
//	@Test
//	public void queryEmpasy() {
//		String name = "empasy.com";
//		Domain domain = getDomain(name);
//		Assert.notNull(domain.getCreateDate(), "Creation Date can not be null");
//	}
//	@Test
//	public void queryEmpas() {
//		String name = "empas.com";
//		Domain domain = getDomain(name);
//		Assert.notNull(domain.getCreateDate(), "Creation Date can not be null");
//	}
//
//
//	private Domain getDomain(String name) {
//		Domain domain = null;
//		Properties props = new Properties();
//		props.setProperty("whois.override.verisign", "e-deliverygroup.com");
//
//		WhoisMonitor monitor = WhoisMonitorFactory.getWhoisMonitor(name, props);
//		try {
//			monitor.init(null, name);
//			domain = monitor.query(name);
//			logger.debug("Domain Info: Name=" + domain.getName() + "|Registrar=" + domain.getRegistrar() + "|Create="
//			+ domain.getCreateDate() + "|Update=" + domain.getUpdateDate() + "|Expiry=" + domain.getExpiryDate());
//		} catch (IOException ioe) {
//			logger.error("Error calling Whois: " + ioe.getMessage(), ioe);
//		}
//		return domain;
//	}
//}