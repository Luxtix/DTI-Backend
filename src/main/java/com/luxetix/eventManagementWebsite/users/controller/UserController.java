package com.luxetix.eventManagementWebsite.users.controller;


import com.luxetix.eventManagementWebsite.response.Response;
import com.luxetix.eventManagementWebsite.users.dto.ProfileRequestDto;
import com.luxetix.eventManagementWebsite.users.dto.ProfileResponseDto;
import com.luxetix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.service.UserService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
@Log
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Response<Users>> register(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return Response.successfulResponse("User registered successfully", userService.register(userRegisterRequestDto));
    }


    @PatchMapping("/profile")
    public ResponseEntity<Response<ProfileResponseDto>> updateProfile(@ModelAttribute ProfileRequestDto profileRequestDto) {
        return Response.successfulResponse("User profile update successfully", userService.updateProfile(profileRequestDto));
    }


    @GetMapping("/profile")
    public ResponseEntity<Response<ProfileResponseDto>> getProfileData(){
        return Response.successfulResponse("Profile data has been fetched",userService.getProfileData());
    }

}
