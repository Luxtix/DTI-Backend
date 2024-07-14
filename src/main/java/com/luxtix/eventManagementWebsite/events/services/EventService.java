package com.luxtix.eventManagementWebsite.events.services;

import com.luxtix.eventManagementWebsite.events.dto.EventDetailDtoResponse;
import com.luxtix.eventManagementWebsite.events.dto.EventListDtoResponse;
import com.luxtix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxtix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    Events addNewEvent(MultipartFile image, NewEventRequestDto newEventRequestDto,String email);


    Page<EventListDtoResponse> getAllEvent(String email, String categoryName, String cityName, String eventName, Boolean eventType, Boolean isOnline, Boolean isFavorite, int page, int page_size);


    Page<EventListDtoResponse> getAllEventPublic(String categoryName, String cityName, String eventName, Boolean eventType, Boolean isOnline, Boolean isFavorite, int page, int page_size);
    
    EventListDtoResponse convertAllEventToDto(Events events, Long userId);

    EventDetailDtoResponse getEventById(String email, Boolean isReferral, long id);


    EventDetailDtoResponse getPublicEventById(long id);

    void deleteEventById(long id);


    Events updateEvent(long id, MultipartFile image, UpdateEventRequestDto data);


    String getFileExtension(String fileName);

    boolean isValidImageExtension(String fileName);

    Events getEventData(long eventId);


    List<Events> getOrganizerEvent(long userId);

}
