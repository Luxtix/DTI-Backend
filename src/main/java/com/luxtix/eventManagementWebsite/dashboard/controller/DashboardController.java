package com.luxtix.eventManagementWebsite.dashboard.controller;

import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventDetailResponseDto;
import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventResponseDto;
import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventSummaryResponseDto;
import com.luxtix.eventManagementWebsite.dashboard.service.DashboardService;
import com.luxtix.eventManagementWebsite.response.Response;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/summary")
@Validated
@Log
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    public ResponseEntity<Response<DashboardEventSummaryResponseDto>> getTransactionSummary(@RequestParam(value = "dateType", defaultValue = "day",required = false) String dateType, @PathVariable("id") long eventId){
        var claims = Claims.getClaimsFromJwt();
        log.info(claims.toString());
        return Response.successfulResponse("All transactions fetched successfully", dashboardService.getSummaryData(eventId,dateType));
    }

    @GetMapping("/event")
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    public ResponseEntity<Response<List<DashboardEventResponseDto>>> getAllOrganizerEvent(){
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("All organizer event fetched successfully", dashboardService.getOrganizerEvent(email));
    }

    @GetMapping("/event/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    public ResponseEntity<Response<DashboardEventDetailResponseDto>> getAllOrganizerEventDetail(@PathVariable("id") long id){
        return Response.successfulResponse("All organizer event fetched successfully", dashboardService.getOrganizerEventDetail(id));
    }


}
