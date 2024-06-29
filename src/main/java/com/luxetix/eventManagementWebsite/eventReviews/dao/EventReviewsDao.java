package com.luxetix.eventManagementWebsite.eventReviews.dao;

public interface EventReviewsDao {

    long getId();
    int getRating();
    String getComment();

    String getReviewCategory();

    String getReviewerName();
}
