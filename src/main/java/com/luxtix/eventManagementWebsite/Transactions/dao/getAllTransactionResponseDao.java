package com.luxtix.eventManagementWebsite.Transactions.dao;

import java.time.LocalDate;
import java.time.LocalTime;

public interface getAllTransactionResponseDao {
    long getId();
    String getEventName();

    String getVenue();

    String getTicketName();

    int getTicketQty();

    LocalDate getEventDate();

    String getCityName();

    LocalTime getStartTime();

    LocalTime getEndTime();

    boolean getIsOnline();


    String getEventImage();
}
