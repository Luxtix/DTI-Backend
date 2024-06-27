package com.luxetix.eventManagementWebsite.events.dao;


import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public interface EventTicketDao {
    Long getTicketId();
    String getTicketName();
    int getTicketPrice();
    int getTicketQuantity();

    LocalDate getStartDate();

    LocalDate getEndDate();

    int getVoucherLimit();

}
