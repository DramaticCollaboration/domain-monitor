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

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.niulbird.domain.Certificate;
import com.niulbird.domain.Domain;
import com.niulbird.domain.certificate.CertificateMonitor;
import com.niulbird.domain.monitor.util.MailUtil;
import com.niulbird.domain.whois.WhoisMonitor;
import com.niulbird.domain.whois.WhoisMonitorFactory;

import freemarker.template.TemplateException;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ImportResource({"classpath*:applicationContext.xml"})
public class MonitorApp implements CommandLineRunner {
	private static Logger logger = Logger.getLogger(Domain.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	@Autowired
	private freemarker.template.Configuration freeMarkerConfiguration;
	
	
	@Override
	public void run(String... args) {
		Proxy proxy = null;
		WhoisMonitor whoisMonitor = null;

		if (env.getProperty("conn.proxy").equalsIgnoreCase("true")) {
			SocketAddress addr = new InetSocketAddress(env.getProperty("conn.proxy.host"),
					Integer.parseInt(env.getProperty("conn.proxy.port")));
			if (env.getProperty("conn.proxy.type").equalsIgnoreCase("socks")) {
				proxy = new Proxy(Proxy.Type.SOCKS, addr);
			} else if (env.getProperty("conn.proxy.type").equalsIgnoreCase("http")) {
				proxy = new Proxy(Proxy.Type.HTTP, addr);
			}
		}

		for (int i = 0; i < Integer.parseInt(env.getProperty("list.no")); i++) {
			Domain domain = null;
			List<String> domainNames = Arrays.asList(env.getProperty(i + ".list.domain").split("\\s*,\\s*"));
			List<Domain> domains = new ArrayList<Domain>();
			for (String domainName : domainNames) {
				whoisMonitor = WhoisMonitorFactory.getWhoisMonitor(proxy, domainName);
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
			
			List<String> certificateHostNames = Arrays.asList(env.getProperty(i + ".list.cert").split("\\s*,\\s*"));
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
			
			mailUtil.sendMail(mailSender, env.getProperty(i + ".list.email").split("\\s*,\\s*"), body, env);
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
	
	public static void main(String[] args) {
        SpringApplication.run(MonitorApp.class, args);
    }
}