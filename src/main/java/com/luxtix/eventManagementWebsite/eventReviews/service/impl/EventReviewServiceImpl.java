package com.luxtix.eventManagementWebsite.eventReviews.service.impl;


import com.luxtix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxtix.eventManagementWebsite.eventReviews.dto.EventReviewsDto;
import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviews;
import com.luxtix.eventManagementWebsite.eventReviews.repository.EventReviewsRepository;
import com.luxtix.eventManagementWebsite.eventReviews.service.EventReviewService;
import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventReviewServiceImpl implements EventReviewService {

    private final EventReviewsRepository eventReviewsRepository;

    public EventReviewServiceImpl(EventReviewsRepository eventReviewsRepository) {
        this.eventReviewsRepository = eventReviewsRepository;
    }

    @Override
    public List<EventReviewsDto> getEventReviews(long id) {
        List<EventReviews> reviewData = eventReviewsRepository.findByEventsId(id).orElseThrow(() -> new DataNotFoundException("Review is empty"));
        List<EventReviewsDto> reviewList = new ArrayList<>();
        for(EventReviews reviews : reviewData){
            EventReviewsDto newReview = new EventReviewsDto();
            newReview.setId(reviews.getId());
            newReview.setReviewerName(reviews.getUsers().getFullname());
            newReview.setComments(reviews.getComment());
            newReview.setRating(reviews.getRating());
            newReview.setType(reviews.getReviewCategory().toString());
            reviewList.add(newReview);
        }
        return reviewList;

    }

    @Override
    public void addNewReview(EventReviews eventReviews) {
        eventReviewsRepository.save(eventReviews);
    }
}
