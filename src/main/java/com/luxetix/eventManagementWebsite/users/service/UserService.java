package com.luxetix.eventManagementWebsite.users.service;

import com.luxetix.eventManagementWebsite.users.dto.ProfileRequestDto;
import com.luxetix.eventManagementWebsite.users.dto.ProfileResponseDto;
import com.luxetix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import org.apache.catalina.User;

public interface UserService {
    Users register(UserRegisterRequestDto user);

    ProfileResponseDto updateProfile(ProfileRequestDto profileRequestDto);

    ProfileResponseDto getProfileData();
}
