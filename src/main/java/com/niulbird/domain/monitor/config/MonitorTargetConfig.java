package com.niulbird.domain.monitor.config;

import com.niulbird.domain.monitor.model.MonitorEmail;
import com.niulbird.domain.monitor.model.MonitorProxy;
import com.niulbird.domain.monitor.model.MonitorTarget;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@ConfigurationProperties(prefix = "monitor")
@Data
public class MonitorTargetConfig {
    private List<Integer> alertPeriod;
    private List<String> whoisOverrideVerisign;
    private MonitorProxy proxy;
    private MonitorEmail email;
    private List<MonitorTarget> targets;
}
