package com.niulbird.domain.monitor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ImportResource({"classpath*:applicationContext.xml"})
public class MonitorApp extends BaseMonitor implements CommandLineRunner {
	@Override
	public void run(String... args) {
		if (args.length >= 1 && args[0].equalsIgnoreCase("true")) {
			execute(true);
		} else {
			execute(false);
		}
	}
	
	public static void main(String[] args) {
        SpringApplication.run(MonitorApp.class, args).close();
    }
}