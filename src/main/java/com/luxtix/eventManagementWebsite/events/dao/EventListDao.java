package com.luxtix.eventManagementWebsite.events.dao;

import java.time.LocalDate;
import java.time.LocalTime;



public interface EventListDao {

    Long getEventId();

    String getEventName();
    Integer getTicketPrice();
    String getPriceCategory();
    String getCategoryName();
    String getCityName();

    String getAddress();
    String getVenueName();
    String getEventImage();
    String getDescriptions();
    Boolean getIsOnline();
    int getFavoriteCount();
    Boolean getIsFavorite();
    LocalDate getEventDate();
    LocalTime getStartTime();
    LocalTime getEndTime();


}
