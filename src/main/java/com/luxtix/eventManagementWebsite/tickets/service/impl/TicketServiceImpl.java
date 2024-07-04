package com.luxtix.eventManagementWebsite.tickets.service.impl;

import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxtix.eventManagementWebsite.tickets.repository.TicketRepository;
import com.luxtix.eventManagementWebsite.tickets.service.TicketService;
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
