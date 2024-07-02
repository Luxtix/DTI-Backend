package com.luxetix.eventManagementWebsite;

import com.luxetix.eventManagementWebsite.config.RsaConfigProperties;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

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
		log.info("Luxetix Backend Application started successfully");
	}
}
