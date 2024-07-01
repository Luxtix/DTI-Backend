package com.luxetix.eventManagementWebsite.eventReviews.dto;

import com.luxetix.eventManagementWebsite.eventReviews.entitity.EventReviewType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ReviewEventRequestDto {
    private long eventId;
    private int rating;
    private String comments;

    @Enumerated(EnumType.STRING)
    private EventReviewType type;
}
