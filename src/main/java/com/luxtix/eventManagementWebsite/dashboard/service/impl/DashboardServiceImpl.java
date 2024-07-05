package com.luxtix.eventManagementWebsite.dashboard.service.impl;

import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventSummaryResponseDto;
import com.luxtix.eventManagementWebsite.dashboard.service.DashboardService;
import com.luxtix.eventManagementWebsite.eventReviews.dto.EventReviewsDto;
import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviews;
import com.luxtix.eventManagementWebsite.eventReviews.service.EventReviewService;
import com.luxtix.eventManagementWebsite.events.dao.EventSummaryDao;
import com.luxtix.eventManagementWebsite.events.services.EventService;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;
import com.luxtix.eventManagementWebsite.tickets.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DashboardServiceImpl implements DashboardService {


    private final TicketService ticketService;


    private final EventService eventService;

    public DashboardServiceImpl(TicketService ticketService, EventService eventService) {
        this.ticketService = ticketService;
        this.eventService = eventService;
    }

    @Override
    public DashboardEventSummaryResponseDto getSummaryData(long eventId, String dateType) {
       List<TicketSummaryDao> ticketData = ticketService.getTicketSummaryData(eventId,dateType);
       DashboardEventSummaryResponseDto resp = new DashboardEventSummaryResponseDto();
       EventSummaryDao eventData = eventService.getEventSummaryData(eventId);
       resp.setName(eventData.getName());
       resp.setCity(eventData.getCityName());
       resp.setAddress(eventData.getAddress());
       resp.setEventDate(eventData.getEventDate());
       resp.setStartTime(eventData.getStartTime());
       resp.setEndTime(eventData.getEndTime());
       resp.setRevenue(eventData.getRevenue());
       resp.setVenue(eventData.getVenue());
       resp.setRating(eventData.getAverageRating());
       resp.setTicketQty(eventData.getTicketQty());
       resp.setRemainingTicket(eventData.getRemainingTicket());
       resp.setTickets(ticketData);
       return resp;
    }
}
