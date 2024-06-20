package com.luxetix.eventManagementWebsite.events.services.impl;

import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.categories.Categories;
import com.luxetix.eventManagementWebsite.categories.repository.CategoryRepository;
import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.events.repository.EventRepository;
import com.luxetix.eventManagementWebsite.events.services.EventService;
import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxetix.eventManagementWebsite.tickets.repository.TicketRepository;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;


@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;


    private final TicketRepository ticketRepository;
    private final UserRepository  userRepository;
    private final CategoryRepository categoryRepository;

    public EventServiceImpl(EventRepository eventRepository, TicketRepository ticketRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Events addNewEvent(NewEventRequestDto event) {
        Categories category = categoryRepository.findById(event.getCategory()).orElseThrow(() -> new DataNotFoundException("Category with ID " + event.getCategory() + " is not found"));
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("Organizer is not found"));
        Events newEvent = event.toEntity();
        newEvent.setOrganizers(userData.getOrganizers());
        newEvent.setCategories(category);
        Tickets newTicket = new Tickets();
        newTicket.setQty(event.getTicketQty());
        newTicket.setPrice(event.getTickerPrice());
        newTicket.setName(event.getTicketName());
        ticketRepository.save(newTicket);
        return newEvent;
    }
}
