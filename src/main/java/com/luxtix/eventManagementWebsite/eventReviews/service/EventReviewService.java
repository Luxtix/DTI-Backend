package com.luxtix.eventManagementWebsite.eventReviews.service;

import com.luxtix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviews;

import java.util.List;

public interface EventReviewService {
    List<EventReviewsDao> getEventReviews(long eventId);

    void addNewReview(EventReviews eventReviews);
}
