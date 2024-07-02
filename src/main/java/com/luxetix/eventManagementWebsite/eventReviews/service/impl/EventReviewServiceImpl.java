package com.luxetix.eventManagementWebsite.eventReviews.service.impl;


import com.luxetix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxetix.eventManagementWebsite.eventReviews.entity.EventReviews;
import com.luxetix.eventManagementWebsite.eventReviews.repository.EventReviewsRepository;
import com.luxetix.eventManagementWebsite.eventReviews.service.EventReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventReviewServiceImpl implements EventReviewService {

    private final EventReviewsRepository eventReviewsRepository;

    public EventReviewServiceImpl(EventReviewsRepository eventReviewsRepository) {
        this.eventReviewsRepository = eventReviewsRepository;
    }

    @Override
    public List<EventReviewsDao> getEventReviews(long eventId) {
        return eventReviewsRepository.getEventReviews(eventId);
    }

    @Override
    public void addNewReview(EventReviews eventReviews) {
        eventReviewsRepository.save(eventReviews);
    }
}
