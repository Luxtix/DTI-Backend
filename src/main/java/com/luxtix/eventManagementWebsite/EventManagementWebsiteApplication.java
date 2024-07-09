package com.luxtix.eventManagementWebsite;

import com.luxtix.eventManagementWebsite.config.RsaConfigProperties;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@EnableConfigurationProperties(RsaConfigProperties.class)
@SpringBootApplication
@Log
@EnableCaching
public class EventManagementWebsiteApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(EventManagementWebsiteApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		log.info("Luxtix Backend Application started successfully");
	}
}
