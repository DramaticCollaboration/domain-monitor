package com.niulbird.domain.monitor.model;

import lombok.Data;

import java.util.List;

@Data
public class MonitorTarget {
    List<String> certs;
    List<String> domains;
    List<String> emails;
}
