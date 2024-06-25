package com.luxetix.eventManagementWebsite.events.services;

import com.luxetix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.EventType;
import com.luxetix.eventManagementWebsite.events.entity.Events;

import java.util.List;

public interface EventService {
    Events addNewEvent(NewEventRequestDto newEventRequestDto);

    List<GetEventListDtoResponse> getAllEvent( List<String> categoryName, String cityName,String eventName, String eventType, Boolean isOnline, Boolean isFavorite, int page, int page_size);

    Events getEventById(long id);
}
