package com.luxtix.eventManagementWebsite.tickets.service;

import com.luxtix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;

import java.util.List;

public interface TicketService {
    void addNewTicket(Tickets tickets);

    List<TicketDao> getEventTicket(long eventId);

    Tickets getEventTicketById(long id);
}
