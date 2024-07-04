package com.luxtix.eventManagementWebsite.events.dao;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EventDetailDao {
    Long getEventId();
    String getEventName();
    String getCityName();
    String getAddress();
    String getEventImage();
    String getVenueName();
    String getDescription();
    LocalDate getEventDate();
    LocalTime getStartTime();
    LocalTime getEndTime();
    Boolean getIsOnline();
    String getPriceCategory();
    String getOrganizerName();
    String getOrganizerAvatar();
    Integer getFavoriteCounts();
    Boolean getIsFavorite();

    Boolean getIsDone();
}
