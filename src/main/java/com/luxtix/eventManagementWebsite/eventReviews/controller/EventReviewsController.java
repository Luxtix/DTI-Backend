package com.luxtix.eventManagementWebsite.eventReviews.controller;


import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventSummaryResponseDto;
import com.luxtix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxtix.eventManagementWebsite.eventReviews.dto.EventReviewsDto;
import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviews;
import com.luxtix.eventManagementWebsite.eventReviews.service.EventReviewService;
import com.luxtix.eventManagementWebsite.response.Response;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-review")
@Validated
@Log
public class EventReviewsController {
    private final EventReviewService eventReviewService;

    public EventReviewsController(EventReviewService eventReviewService) {
        this.eventReviewService = eventReviewService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<List<EventReviewsDto>>> getEventReviewData(@PathVariable("id") long eventId){
        return Response.successfulResponse("All event review fetched successfully", eventReviewService.getEventReviews(eventId));
    }
}
