package com.luxetix.eventManagementWebsite.events.services;

import com.luxetix.eventManagementWebsite.eventReviews.dto.ReviewEventRequestDto;
import com.luxetix.eventManagementWebsite.eventReviews.dto.ReviewEventResponseDto;
import com.luxetix.eventManagementWebsite.eventReviews.entitity.EventReviews;
import com.luxetix.eventManagementWebsite.events.dao.EventListDao;
import com.luxetix.eventManagementWebsite.events.dto.EventDetailDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    Events addNewEvent(MultipartFile image, NewEventRequestDto newEventRequestDto);

    Page<EventListDao> getAllEvent(String categoryName, String cityName, String eventName, Boolean eventType, Boolean isOnline, Boolean isFavorite, int page, int page_size);
    List<GetEventListDtoResponse> convertAllEventToDto(Page<EventListDao> data);

    EventDetailDtoResponse getEventById(long id);

    void deleteEventById(long id);


    Events updateEvent(long id, MultipartFile image, UpdateEventRequestDto data);

    ReviewEventResponseDto addReview(ReviewEventRequestDto data);
}
