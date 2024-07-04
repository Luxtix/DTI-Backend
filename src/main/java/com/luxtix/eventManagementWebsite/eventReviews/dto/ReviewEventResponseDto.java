package com.luxtix.eventManagementWebsite.eventReviews.dto;

import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviewType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;


@Data
public class ReviewEventResponseDto {
    private long id;
    private String name;
    private int rating;
    private String comment;

    @Enumerated(EnumType.STRING)
    private EventReviewType type;
}
