package com.luxtix.eventManagementWebsite.eventReviews.service.impl;


import com.luxtix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxtix.eventManagementWebsite.eventReviews.dto.EventReviewsDto;
import com.luxtix.eventManagementWebsite.eventReviews.dto.ReviewEventRequestDto;
import com.luxtix.eventManagementWebsite.eventReviews.dto.ReviewEventResponseDto;
import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviews;
import com.luxtix.eventManagementWebsite.eventReviews.repository.EventReviewsRepository;
import com.luxtix.eventManagementWebsite.eventReviews.service.EventReviewService;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.events.services.EventService;
import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventReviewServiceImpl implements EventReviewService {

    private final EventReviewsRepository eventReviewsRepository;

    private final UserService userService;

    private final EventService eventService;

    public EventReviewServiceImpl(EventReviewsRepository eventReviewsRepository, UserService userService, @Lazy EventService eventService) {
        this.eventReviewsRepository = eventReviewsRepository;
        this.userService = userService;
        this.eventService = eventService;
    }

    @Override
    public Page<EventReviews> getEventReviews(long id, int page, int page_size ) {
        Pageable pageable = PageRequest.of(page, page_size);
        return eventReviewsRepository.findByEventsId(id,pageable).orElseThrow(() -> new DataNotFoundException("Review is empty"));
    }

    @Override
    public List<EventReviewsDto> convertAllEventReviewsToDto(Page<EventReviews> data) {
        List<EventReviewsDto> reviewList = new ArrayList<>();
        for(EventReviews reviews : data){
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
    public ReviewEventResponseDto addNewReview(String email, ReviewEventRequestDto data) {
        Users userData = userService.getUserByEmail(email);
        EventReviews reviewData = new EventReviews();
        Events eventData = eventService.getEventData(data.getId());
        reviewData.setEvents(eventData);
        reviewData.setRating(data.getRating());
        reviewData.setUsers(userData);
        reviewData.setReviewCategory(data.getType());
        reviewData.setComment(data.getComments());
        eventReviewsRepository.save(reviewData);
        ReviewEventResponseDto resp = new ReviewEventResponseDto();
        resp.setId(reviewData.getId());
        resp.setComment(reviewData.getComment());
        resp.setType(reviewData.getReviewCategory());
        resp.setRating(reviewData.getRating());
        resp.setName(reviewData.getUsers().getFullname());
        return resp;
    }
}
