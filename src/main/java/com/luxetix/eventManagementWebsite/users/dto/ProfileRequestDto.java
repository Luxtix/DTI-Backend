package com.luxetix.eventManagementWebsite.users.dto;


import com.luxetix.eventManagementWebsite.users.entity.Users;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileRequestDto {
    private String fullname;
    private String phoneNumber;
    private MultipartFile avatar;
}
