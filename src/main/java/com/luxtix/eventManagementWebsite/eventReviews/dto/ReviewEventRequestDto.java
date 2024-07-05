package com.luxtix.eventManagementWebsite.eventReviews.dto;

import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviewType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ReviewEventRequestDto {
    private long id;
    private int rating;
    private String comments;

    @Enumerated(EnumType.STRING)
    private EventReviewType type;
}
