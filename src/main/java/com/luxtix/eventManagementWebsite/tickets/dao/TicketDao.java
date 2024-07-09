package com.luxtix.eventManagementWebsite.tickets.dao;

public interface TicketDao {
    Long getTicketId();
    String getTicketName();
    int getTicketPrice();
    int getTicketQuantity();

    int getRemainingQty();

}