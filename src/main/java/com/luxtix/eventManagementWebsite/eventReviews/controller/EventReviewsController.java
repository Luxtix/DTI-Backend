package com.luxtix.eventManagementWebsite.eventReviews.controller;


import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.eventReviews.dto.EventReviewsDto;
import com.luxtix.eventManagementWebsite.eventReviews.dto.ReviewEventRequestDto;
import com.luxtix.eventManagementWebsite.eventReviews.dto.ReviewEventResponseDto;
import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviews;
import com.luxtix.eventManagementWebsite.eventReviews.service.EventReviewService;
import com.luxtix.eventManagementWebsite.response.Response;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    public ResponseEntity<Response<List<EventReviewsDto>>> getEventReviewData(@PathVariable("id") long eventId,@RequestParam(defaultValue = "0",required = false) int page, @RequestParam(defaultValue = "10",required = false) int size){
        Page<EventReviews> reviewData = eventReviewService.getEventReviews(eventId,page,size);
        List<EventReviewsDto> resp = eventReviewService.convertAllEventReviewsToDto(reviewData);
        return Response.successfulResponseWithPage(HttpStatus.OK.value(), "All event review fetched successfully",resp,reviewData.getTotalPages(),reviewData.getTotalElements(),reviewData.getNumber());
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Response<ReviewEventResponseDto>> addNewReview(@Validated @RequestBody ReviewEventRequestDto data) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("Event review add successfully",eventReviewService.addNewReview(email,data));
    }
}
