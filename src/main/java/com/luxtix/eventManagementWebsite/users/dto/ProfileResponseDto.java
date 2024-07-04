package com.luxtix.eventManagementWebsite.users.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;


@Data
public class ProfileResponseDto implements Serializable {
    private String email;
    private String fullname;
    private String phoneNumber;
    private String avatar;
    private String refferalCode;
}
