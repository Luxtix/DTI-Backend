package com.luxtix.eventManagementWebsite.users.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileRequestDto {
    @NotBlank(message = "Display name is required")
    private String displayName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;


    private MultipartFile avatar;
}
