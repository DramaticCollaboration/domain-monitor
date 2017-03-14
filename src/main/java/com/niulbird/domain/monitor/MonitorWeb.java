package com.niulbird.domain.monitor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MonitorWeb extends BaseMonitor {

	@Scheduled(cron="0 0 9 * * MON")
	public void runMonitorReport() {
		logger.debug("Running monitor.");
		execute(true);
	}
	
	@Scheduled(cron="0 0 9 * * SUN,TUE-SAT")
	public void runMonitorAlert() {
		logger.debug("Running monitor.");
		execute(false);
	}
}