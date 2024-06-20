package com.luxetix.eventManagementWebsite.events.services;

import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.Events;

public interface EventService {
    Events addNewEvent(NewEventRequestDto newEventRequestDto);
}
