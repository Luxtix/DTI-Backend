package com.luxtix.eventManagementWebsite.eventReviews.service;

import com.luxtix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxtix.eventManagementWebsite.eventReviews.dto.EventReviewsDto;
import com.luxtix.eventManagementWebsite.eventReviews.dto.ReviewEventRequestDto;
import com.luxtix.eventManagementWebsite.eventReviews.dto.ReviewEventResponseDto;
import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviews;
import com.luxtix.eventManagementWebsite.events.dao.EventListDao;
import com.luxtix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EventReviewService {
    Page<EventReviews> getEventReviews(long eventId, int page, int page_size);


    List<EventReviewsDto> convertAllEventReviewsToDto(Page<EventReviews> data);

    ReviewEventResponseDto addNewReview(String email, ReviewEventRequestDto data);
}
