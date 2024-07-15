package com.luxtix.eventManagementWebsite.pointHistory.controller;


import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.pointHistory.dto.PointHistoryResponseDto;
import com.luxtix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxtix.eventManagementWebsite.response.Response;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Response<PointHistoryResponseDto>> getPoints() {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("User point fetched successfully", pointHistoryService.getUserPoint(email));
    }
}
