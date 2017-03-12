package com.niulbird.domain.whois;

import java.io.IOException;
import java.net.Proxy;

import com.niulbird.domain.Domain;

public interface WhoisMonitorIF {
	public Domain parseDomain(String info);
	public void init(Proxy proxy, String name) throws IOException;
}
