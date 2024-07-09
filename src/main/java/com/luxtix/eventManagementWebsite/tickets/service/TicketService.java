package com.luxtix.eventManagementWebsite.tickets.service;

import com.luxtix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxtix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;
import com.luxtix.eventManagementWebsite.tickets.dto.TicketDto;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;

import java.util.List;

public interface TicketService {
    void createNewTicket(Tickets newTickets);
    void updateTicket(Tickets updatedTickets);

    List<TicketDto> getEventTicket(long eventId);

    Tickets getEventTicketById(long id);
    void deleteTicketById(long id);

    List<TicketSummaryDao> getTicketSummaryData(long userId, String dateType);
}
