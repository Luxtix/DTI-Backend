package com.luxetix.eventManagementWebsite.eventReviews.service;

import com.luxetix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxetix.eventManagementWebsite.eventReviews.entity.EventReviews;

import java.util.List;

public interface EventReviewService {
    List<EventReviewsDao> getEventReviews(long eventId);

    void addNewReview(EventReviews eventReviews);
}
