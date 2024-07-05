package com.luxtix.eventManagementWebsite.dashboard.controller;

import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventSummaryResponseDto;
import com.luxtix.eventManagementWebsite.dashboard.service.DashboardService;
import com.luxtix.eventManagementWebsite.response.Response;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Response<DashboardEventSummaryResponseDto>> getTransactionSummary(@RequestParam("dateType") String dateType, @PathVariable("id") long eventId){
        return Response.successfulResponse("All transactions fetched successfully", dashboardService.getSummaryData(eventId,dateType));
    }
}
