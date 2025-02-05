package com.niulbird.domain.monitor;

import java.security.cert.X509Certificate;


import com.niulbird.domain.monitor.certificate.CertificateMonitor;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class CertificateMonitorTest extends BaseTestCase { 
	
	@Test
	public void queryEmpasy() {
		String name = "empasy.com";
		CertificateMonitor certificateMonitor = new CertificateMonitor();
		X509Certificate certificate = certificateMonitor.query(name);
		Assert.notNull(certificate.getNotAfter(), "NotAfter Can not be null");
	}
}