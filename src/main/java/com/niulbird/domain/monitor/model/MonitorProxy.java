package com.niulbird.domain.monitor.model;

import lombok.Data;

import java.util.List;

@Data
public class MonitorProxy {
    String host;
    Integer port;
    String type;
}
