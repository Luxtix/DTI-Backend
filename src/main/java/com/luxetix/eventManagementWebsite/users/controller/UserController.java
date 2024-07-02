package com.luxetix.eventManagementWebsite.users.controller;


import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.response.Response;
import com.luxetix.eventManagementWebsite.users.dto.*;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.service.UserService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    @PutMapping("/profile")
    public ResponseEntity<Response<ProfileResponseDto>> updateProfile(@ModelAttribute ProfileRequestDto profileRequestDto) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("User profile update successfully", userService.updateProfile(email,profileRequestDto));
    }


    @GetMapping("/profile")
    public ResponseEntity<Response<ProfileResponseDto>> getProfileData(){
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("Profile data has been fetched",userService.getProfileData(email));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Response<ChangePasswordResponseDto>> changePassword(@RequestBody ChangePasswordRequestDto data){
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("Password has been successfully change", userService.changePassword(data,email));
    }


}
