package com.luxtix.eventManagementWebsite.eventReviews.dto;

import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviewType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewEventRequestDto {
    @NotNull(message = "Event name is required")
    private long id;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotNull(message = "Comments are required")
    @Size(min = 1, message = "Comments cannot be empty")
    private String comments;


    @NotNull(message = "Review type is required")
    @Enumerated(EnumType.STRING)
    private EventReviewType type;
}
