package com.luxtix.eventManagementWebsite.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class CloudinaryConfiguration {
    @Value("dv9bbdl6i")
    private String cloudName;

    @Value("298575313282357")
    private String apiKey;

    @Value("p_mRglFqQ5_MfcS-YypN9XBx5Uo")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }
}
