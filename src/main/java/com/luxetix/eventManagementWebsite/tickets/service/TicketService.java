package com.luxetix.eventManagementWebsite.tickets.service;

import com.luxetix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;

import java.util.List;

public interface TicketService {
    void addNewTicket(Tickets tickets);

    List<TicketDao> getEventTicket(long eventId);

    Tickets getEventTicketById(long id);
}
