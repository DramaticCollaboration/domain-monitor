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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.niulbird.domain.Certificate;
import com.niulbird.domain.Domain;
import com.niulbird.domain.monitor.certificate.CertificateMonitor;
import com.niulbird.domain.monitor.util.MailUtil;
import com.niulbird.domain.monitor.whois.WhoisMonitor;
import com.niulbird.domain.monitor.whois.WhoisMonitorFactory;

import freemarker.template.TemplateException;

public abstract class BaseMonitor {
    protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private Properties props;
	
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	@Autowired
	private freemarker.template.Configuration freeMarkerConfiguration;
	
    protected void execute() {
		Proxy proxy = null;
		WhoisMonitor whoisMonitor = null;

		if (props.getProperty("conn.proxy").equalsIgnoreCase("true")) {
			SocketAddress addr = new InetSocketAddress(props.getProperty("conn.proxy.host"),
					Integer.parseInt(props.getProperty("conn.proxy.port")));
			if (props.getProperty("conn.proxy.type").equalsIgnoreCase("socks")) {
				proxy = new Proxy(Proxy.Type.SOCKS, addr);
				System.setProperty("socksProxyHost", props.getProperty("conn.proxy.host"));
		        System.setProperty("socksProxyPort", props.getProperty("conn.proxy.port"));
			} else if (props.getProperty("conn.proxy.type").equalsIgnoreCase("http")) {
				proxy = new Proxy(Proxy.Type.HTTP, addr);
				System.setProperty("https.proxyPort", props.getProperty("conn.proxy.host"));
				System.setProperty("https.proxyPort", props.getProperty("conn.proxy.port"));
			}
		}

		for (int i = 0; i < Integer.parseInt(props.getProperty("list.no")); i++) {
			Domain domain = null;
			List<String> domainNames = Arrays.asList(props.getProperty(i + ".list.domain").split("\\s*,\\s*"));
			List<Domain> domains = new ArrayList<Domain>();
			for (String domainName : domainNames) {
				whoisMonitor = WhoisMonitorFactory.getWhoisMonitor(domainName);
				try {
					whoisMonitor.init(proxy, domainName);

					domain = whoisMonitor.query(domainName);
					logger.debug("Domain Info: Name=" + domain.getName() + "|Registrar=" + domain.getRegistrar()
							+ "|Create=" + domain.getCreateDate() + "|Update=" + domain.getUpdateDate() + "|Expiry="
							+ domain.getExpiryDate());

					if (ArrayUtils.contains(new int[] { 5, 10, 30, 60 }, domain.getExpiryDays())) {
						logger.debug("HELLO" + domain.getExpiryDays());
					}

					whoisMonitor.disconnect();
				} catch (IOException ioe) {
					logger.error("Error on initialization: " + ioe.getMessage(), ioe);
				}
				domains.add(domain);
			}
			
			List<String> certificateHostNames = Arrays.asList(props.getProperty(i + ".list.cert").split("\\s*,\\s*"));
			List<Certificate> certificates = new ArrayList<Certificate>();
			for (String certificateHostName : certificateHostNames) {
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
			}
			
			MailUtil mailUtil = new MailUtil();

			Collections.sort(certificates);
			Collections.sort(domains);
			
			String body = "";
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put( "domains", domains);
				map.put("certificates", certificates);
				body = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfiguration.getTemplate("summary.ftl"), map);
			} catch (IOException | TemplateException e) {
				logger.error("Error creating template: " + e.getMessage(), e);
			}
			
			mailUtil.sendMail(mailSender, props.getProperty(i + ".list.email").split("\\s*,\\s*"), body, props);
		}
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
