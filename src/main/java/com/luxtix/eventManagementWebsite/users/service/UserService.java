package com.luxtix.eventManagementWebsite.users.service;
import com.luxtix.eventManagementWebsite.users.dto.*;
import com.luxtix.eventManagementWebsite.users.entity.Users;

public interface UserService {
    Users register(UserRegisterRequestDto user);

    ProfileResponseDto updateProfile(String email, ProfileRequestDto profileRequestDto);

    ProfileResponseDto getProfileData(String email);

    ChangePasswordResponseDto changePassword(ChangePasswordRequestDto data, String email);

    Users getUserByEmail(String email);
}
