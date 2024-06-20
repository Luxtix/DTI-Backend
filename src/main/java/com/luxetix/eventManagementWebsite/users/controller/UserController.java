package com.luxetix.eventManagementWebsite.users.controller;


import com.luxetix.eventManagementWebsite.response.Response;
import com.luxetix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.service.UserService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
