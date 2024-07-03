package com.luxetix.eventManagementWebsite.pointHistory.controller;


import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.pointHistory.dto.PointHistoryResponseDto;
import com.luxetix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxetix.eventManagementWebsite.response.Response;
import com.luxetix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/points")
@Validated
@Log
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    public PointHistoryController(PointHistoryService pointHistoryService) {
        this.pointHistoryService = pointHistoryService;
    }


    @GetMapping
    public ResponseEntity<Response<PointHistoryResponseDto>> register() {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("User point fetched successfully", pointHistoryService.getUserPoint(email));
    }
}
