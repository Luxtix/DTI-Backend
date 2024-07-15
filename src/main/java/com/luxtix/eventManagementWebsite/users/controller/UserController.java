package com.luxtix.eventManagementWebsite.users.controller;


import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.response.Response;
import com.luxtix.eventManagementWebsite.users.dto.*;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Response<Users>> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return Response.successfulResponse("User registered successfully", userService.register(userRegisterRequestDto));
    }


    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('SCOPE_USER') or hasAuthority('SCOPE_ORGANIZER')")
    public ResponseEntity<Response<ProfileResponseDto>> updateProfile(@ModelAttribute ProfileRequestDto profileRequestDto) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("User profile update successfully", userService.updateProfile(email,profileRequestDto));
    }



    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('SCOPE_USER') or hasAuthority('SCOPE_ORGANIZER')")
    public ResponseEntity<Response<ProfileResponseDto>> getProfileData(){
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("Profile data has been fetched",userService.getProfileData(email));
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasAuthority('SCOPE_USER') or hasAuthority('SCOPE_ORGANIZER')")
    public ResponseEntity<Response<ChangePasswordResponseDto>> changePassword(@Valid @RequestBody ChangePasswordRequestDto data){
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("Password has been successfully change", userService.changePassword(data,email));
    }


}
