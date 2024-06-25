package com.luxetix.eventManagementWebsite.users.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;



@Data
public class ProfileResponseDto {
    private String email;
    private String username;
    private String fullname;
    private String phoneNumber;
    private String avatar;
    private long totalPoint;
    private String refferalCode;
}
