package com.niulbird.domain.monitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import com.niulbird.domain.monitor.config.MonitorTargetConfig;
import com.niulbird.domain.monitor.model.MonitorProxy;
import com.niulbird.domain.monitor.model.MonitorTarget;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.niulbird.domain.monitor.model.Certificate;
import com.niulbird.domain.monitor.model.Domain;
import com.niulbird.domain.monitor.certificate.CertificateMonitor;
import com.niulbird.domain.monitor.util.MailUtil;
import com.niulbird.domain.monitor.whois.WhoisMonitor;
import com.niulbird.domain.monitor.whois.WhoisMonitorFactory;

import freemarker.template.TemplateException;
import org.springframework.util.CollectionUtils;

public abstract class BaseMonitor {
    protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private MonitorTargetConfig targetConfig;

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private freemarker.template.Configuration freeMarkerConfiguration;
	
    protected void execute(boolean summaryReport) {
		WhoisMonitor whoisMonitor = null;
		MailUtil mailUtil = new MailUtil();

		Proxy proxy = getProxy();


		for (MonitorTarget target : targetConfig.getTargets()) {
			List<Domain> domains = getDomains(target,  mailUtil, whoisMonitor, proxy);
			List<Certificate> certificates = getCertificates(target, mailUtil);

			Collections.sort(certificates);
			Collections.sort(domains);
			
			if (summaryReport) {
				String body = "";
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("domains", domains);
					map.put("certificates", certificates);
					body = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfiguration.getTemplate("summary.ftl"), map);
				} catch (IOException | TemplateException e) {
					logger.error("Error creating template: " + e.getMessage(), e);
				}
			
				mailUtil.sendMail(mailSender, target.getEmails().toArray(new String[0]), body, targetConfig.getEmail());
			}
		}
	}
    
    private Proxy getProxy() {
    	Proxy proxy = null;
    	if (targetConfig.getProxy() != null) {
			MonitorProxy config = targetConfig.getProxy();
			SocketAddress addr = new InetSocketAddress(config.getHost(), config.getPort());
			if (config.getType().equalsIgnoreCase("socks")) {
				proxy = new Proxy(Proxy.Type.SOCKS, addr);
				System.setProperty("socksProxyHost", config.getHost());
		        System.setProperty("socksProxyPort", String.valueOf(config.getPort()));
			} else if (config.getType().equalsIgnoreCase("http")) {
				proxy = new Proxy(Proxy.Type.HTTP, addr);
				System.setProperty("https.proxyPort", config.getHost());
				System.setProperty("https.proxyPort", String.valueOf(config.getPort()));
			}
		}
    	return proxy;
    }
    
    private List<Domain> getDomains(MonitorTarget target, MailUtil mailUtil, WhoisMonitor whoisMonitor, Proxy proxy) {
		Domain domain = null;
		List<Domain> domains = new ArrayList<Domain>();
    	for (String domainName : target.getDomains()) {
			whoisMonitor = WhoisMonitorFactory.getWhoisMonitor(domainName, targetConfig.getWhoisOverrideVerisign());
			try {
				whoisMonitor.init(proxy, domainName);

				domain = whoisMonitor.query(domainName);
				logger.info("Domain Info: Name=" + domain.getName() + "|Registrar=" + domain.getRegistrar()
						+ "|Create=" + domain.getCreateDate() + "|Update=" + domain.getUpdateDate() + "|Expiry="
						+ domain.getExpiryDate());

				if (!CollectionUtils.isEmpty(targetConfig.getAlertPeriod()) && targetConfig.getAlertPeriod().contains(domain.getExpiryDays())) {
					String body = "";
					try {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put( "domain", domain);
						body = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfiguration.getTemplate("domain-expiry-alert.ftl"), map);
					} catch (IOException | TemplateException e) {
						logger.error("Error creating template: " + e.getMessage(), e);
					}
					
					mailUtil.sendMail(mailSender, target.getEmails().toArray(new String[0]),  body, targetConfig.getEmail());
				}

				whoisMonitor.disconnect();
			} catch (IOException ioe) {
				logger.error("Error on initialization: " + ioe.getMessage(), ioe);
			}
			if (domain != null && domain.getRegistrar() != null) {
				domains.add(domain);
			}
		}
    	return domains;
    }
    
    private List<Certificate> getCertificates(MonitorTarget target, MailUtil mailUtil) {
    	List<Certificate> certificates = new ArrayList<Certificate>();
    	for (String certificateHostName : target.getCerts()) {
			CertificateMonitor certificateMonitor = new CertificateMonitor();
			X509Certificate x509Certificate = certificateMonitor.query(certificateHostName);
			int expiryDays = Days.daysBetween(new DateTime(Calendar.getInstance().getTime()), new DateTime(x509Certificate.getNotAfter())).getDays();
			Certificate certificate = new Certificate(certificateHostName, 
					getCNFromDN(x509Certificate.getSubjectDN().toString()),
					getCNFromDN(x509Certificate.getIssuerDN().toString()),
					x509Certificate.getNotBefore(),
					x509Certificate.getNotAfter(),
					expiryDays);
			certificates.add(certificate);
			
			if (!CollectionUtils.isEmpty(targetConfig.getAlertPeriod()) && targetConfig.getAlertPeriod().contains(certificate.getExpiryDays())) {
				String body = "";
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put( "certificate", certificate);
					body = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfiguration.getTemplate("certificate-expiry-alert.ftl"), map);
				} catch (IOException | TemplateException e) {
					logger.error("Error creating template: " + e.getMessage(), e);
				}
				
				mailUtil.sendMail(mailSender, target.getEmails().toArray(new String[0]), body, targetConfig.getEmail());
			}
		}
    	return certificates;
    }
	
	private String getCNFromDN(String dn) {
		String cn = null;
		LdapName ln = null;
		
		try {
			ln = new LdapName(dn);
		} catch (InvalidNameException e) {
			logger.error("Error parsing CN: " + e.getMessage(), e);
		}

		for(Rdn rdn : ln.getRdns()) {
		    if(rdn.getType().equalsIgnoreCase("CN")) {
		        cn = (String)rdn.getValue();
		    }
		}
		return cn;
	}
}
