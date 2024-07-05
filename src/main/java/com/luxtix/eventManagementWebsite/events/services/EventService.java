package com.luxtix.eventManagementWebsite.events.services;

import com.luxtix.eventManagementWebsite.eventReviews.dto.ReviewEventRequestDto;
import com.luxtix.eventManagementWebsite.eventReviews.dto.ReviewEventResponseDto;
import com.luxtix.eventManagementWebsite.events.dao.EventListDao;
import com.luxtix.eventManagementWebsite.events.dao.EventSummaryDao;
import com.luxtix.eventManagementWebsite.events.dto.EventDetailDtoResponse;
import com.luxtix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import com.luxtix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxtix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    Events addNewEvent(MultipartFile image, NewEventRequestDto newEventRequestDto,String email);

    Page<EventListDao> getAllEvent(String email, String categoryName, String cityName, String eventName, Boolean eventType, Boolean isOnline, Boolean isFavorite, int page, int page_size);
    List<GetEventListDtoResponse> convertAllEventToDto(Page<EventListDao> data);

    EventDetailDtoResponse getEventById(long id);

    void deleteEventById(long id);


    Events updateEvent(long id, MultipartFile image, UpdateEventRequestDto data);

    ReviewEventResponseDto addReview(String email, ReviewEventRequestDto data);

    String getFileExtension(String fileName);

    boolean isValidImageExtension(String fileName);

    public EventSummaryDao getEventSummaryData(long eventId);

}
