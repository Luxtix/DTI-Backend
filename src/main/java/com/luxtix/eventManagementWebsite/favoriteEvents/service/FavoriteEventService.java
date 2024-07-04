package com.luxtix.eventManagementWebsite.favoriteEvents.service;
import com.luxtix.eventManagementWebsite.favoriteEvents.dto.EventToggleDtoResponse;

public interface FavoriteEventService {
    EventToggleDtoResponse toggleEvent(long eventId, long userId);
}
