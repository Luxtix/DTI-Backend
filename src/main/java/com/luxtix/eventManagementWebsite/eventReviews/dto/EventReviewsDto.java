package com.luxtix.eventManagementWebsite.eventReviews.dto;


import lombok.Data;

@Data
public class EventReviewsDto {
    private long id;
    private int rating;
    private String comments;
    private String type;
    private String reviewerName;
}
