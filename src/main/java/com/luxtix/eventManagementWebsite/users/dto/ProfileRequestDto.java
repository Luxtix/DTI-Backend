package com.luxtix.eventManagementWebsite.users.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileRequestDto {
    private String fullname;
    private String phoneNumber;
    private MultipartFile avatar;
}
