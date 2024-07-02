package com.luxetix.eventManagementWebsite.users.service;

import com.luxetix.eventManagementWebsite.users.dto.*;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import org.apache.catalina.User;

public interface UserService {
    Users register(UserRegisterRequestDto user);

    ProfileResponseDto updateProfile(String email,ProfileRequestDto profileRequestDto);

    ProfileResponseDto getProfileData(String email);

    ChangePasswordResponseDto changePassword(ChangePasswordRequestDto data,String email);
}
