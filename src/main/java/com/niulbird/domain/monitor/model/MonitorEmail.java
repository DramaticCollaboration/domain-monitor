package com.niulbird.domain.monitor.model;

import lombok.Data;

@Data
public class MonitorEmail {
    private String subject;
    private String fromEmail;
    private String fromName;
}
