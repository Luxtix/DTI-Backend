package com.luxtix.eventManagementWebsite.events.dao;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EventSummaryDao {
    long getId();
    String getName();
    String getVenue();
    String getAddress();

    String getCityName();

    LocalDate getEventDate();

    LocalTime getStartTime();

    LocalTime getEndTime();

    int getTicketQty();

    int getRemainingTicket();

    int getRevenue();

    double getAverageRating();


}



