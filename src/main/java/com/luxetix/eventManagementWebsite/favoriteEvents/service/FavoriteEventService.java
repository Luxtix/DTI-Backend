package com.luxetix.eventManagementWebsite.favoriteEvents.service;
import com.luxetix.eventManagementWebsite.favoriteEvents.dto.EventToggleDtoResponse;

public interface FavoriteEventService {
    EventToggleDtoResponse toggleEvent(long eventId, long userId);
}
