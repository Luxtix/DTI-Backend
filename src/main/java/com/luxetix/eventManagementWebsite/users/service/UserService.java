package com.luxetix.eventManagementWebsite.users.service;

import com.luxetix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import com.luxetix.eventManagementWebsite.users.entity.Users;

public interface UserService {
    Users register(UserRegisterRequestDto user);
}
