package com.luxetix.eventManagementWebsite.tickets.service.impl;

import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxetix.eventManagementWebsite.tickets.repository.TicketRepository;
import com.luxetix.eventManagementWebsite.tickets.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void addNewTicket(Tickets tickets) {
        ticketRepository.save(tickets);
    }

    @Override
    public List<TicketDao> getEventTicket(long eventId) {
        return ticketRepository.getEventTicket(eventId);
    }

    @Override
    public Tickets getEventTicketById(long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Ticket with id " + id + " is not found"));
    }
}
