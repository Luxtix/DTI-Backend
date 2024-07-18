package com.luxtix.eventManagementWebsite.tickets.service;
import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventDetailResponseDto;
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

    int getLowestTicketPrice(long eventId);

    int getTicketSoldQuantity(long eventId, String dateFilter);

    int getTotalTicketInEvent(long eventId);
    List<DashboardEventDetailResponseDto.TicketEventDetailDto> getAllTicketEvent(long id);

    List<TicketSummaryDao> getTicketSummaryData(long userId, String dateType);
}
