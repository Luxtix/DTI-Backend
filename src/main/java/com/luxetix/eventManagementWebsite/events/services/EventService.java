package com.luxetix.eventManagementWebsite.events.services;

import com.luxetix.eventManagementWebsite.events.dao.EventListDao;
import com.luxetix.eventManagementWebsite.events.dto.EventDetailDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EventService {
    Events addNewEvent(NewEventRequestDto newEventRequestDto);

    Page<EventListDao> getAllEvent(String categoryName, String cityName, String eventName, Boolean eventType, Boolean isOnline, Boolean isFavorite, int page, int page_size);
    List<GetEventListDtoResponse> convertAllEventToDto(Page<EventListDao> data);

    EventDetailDtoResponse getEventById(long id);
}
