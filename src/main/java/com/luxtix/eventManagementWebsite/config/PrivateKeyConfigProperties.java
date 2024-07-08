package com.luxtix.eventManagementWebsite.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

@ConfigurationProperties(prefix = "wpa")
public record PrivateKeyConfigProperties(String wpaSecretKey) {
}
