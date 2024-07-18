package com.luxtix.eventManagementWebsite.dashboard.service.impl;

import com.luxtix.eventManagementWebsite.Transactions.service.TransactionService;
import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventDetailResponseDto;
import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventResponseDto;
import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventSummaryResponseDto;
import com.luxtix.eventManagementWebsite.dashboard.service.DashboardService;
import com.luxtix.eventManagementWebsite.eventReviews.service.EventReviewService;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.events.services.EventService;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxtix.eventManagementWebsite.tickets.service.TicketService;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Service
public class DashboardServiceImpl implements DashboardService {


    private final TicketService ticketService;


    private final EventService eventService;

    private final UserService userService;

    private final TransactionService transactionService;

    private final VoucherService voucherService;


    private final EventReviewService eventReviewService;

    public DashboardServiceImpl(TicketService ticketService, EventService eventService, UserService userService, TransactionService transactionService, VoucherService voucherService, EventReviewService eventReviewService) {
        this.ticketService = ticketService;
        this.eventService = eventService;
        this.userService = userService;
        this.transactionService = transactionService;
        this.voucherService = voucherService;
        this.eventReviewService = eventReviewService;
    }


    @Override
    public List<DashboardEventResponseDto> getOrganizerEvent(String email){
        Users user = userService.getUserByEmail(email);
        List<Events> eventList = eventService.getOrganizerEvent(user.getId());
        List<DashboardEventResponseDto> list = new ArrayList<>();
        for(Events eventData : eventList){
            DashboardEventResponseDto resp = new DashboardEventResponseDto();
            resp.setId(eventData.getId());
            resp.setEventName(eventData.getName());
            list.add(resp);
        }
        return list;
    }

    @Override
    public DashboardEventDetailResponseDto getOrganizerEventDetail(long id) {
        Events event = eventService.getEventData(id);
        DashboardEventDetailResponseDto data = new DashboardEventDetailResponseDto();
        data.setName(event.getName());
        data.setCategory(event.getCategories().getId());
        data.setCity(event.getCities().getId());
        data.setIsOnline(event.getIsOnline());
        data.setEventDate(event.getEventDate());
        data.setStartTime(event.getStartTime());
        data.setEndTime(event.getEndTime());
        data.setVenue(event.getVenueName());
        data.setAddress(event.getAddress());
        data.setDescription(event.getDescriptions());
        data.setIsPaid(event.getIsPaid());
        List<DashboardEventDetailResponseDto.TicketEventDetailDto> ticketList = ticketService.getAllTicketEvent(id);
        data.setTickets(ticketList);
        List<DashboardEventDetailResponseDto.VoucherEventDetailDto> voucherList = voucherService.getAllEventVoucher(id);
        data.setVouchers(voucherList);
        return data;
    }

    @Override
    public DashboardEventSummaryResponseDto getSummaryData(long eventId, String dateType) {
       List<TicketSummaryDao> ticketData = ticketService.getTicketSummaryData(eventId,dateType);
       Events eventData = eventService.getEventData(eventId);
       DashboardEventSummaryResponseDto resp = new DashboardEventSummaryResponseDto();
       resp.setName(eventData.getName());
       resp.setCity(eventData.getCities().getName());
       resp.setAddress(eventData.getAddress());
        DayOfWeek day = eventData.getEventDate().getDayOfWeek();
        String eventDay = day.getDisplayName(
                java.time.format.TextStyle.FULL,
                Locale.ENGLISH
        );
        resp.setEventDay(eventDay);
       resp.setEventDate(eventData.getEventDate());
       resp.setStartTime(eventData.getStartTime());
       resp.setEndTime(eventData.getEndTime());
       resp.setRevenue(transactionService.getEventTotalRevenue(eventId,dateType));
       resp.setVenue(eventData.getVenueName());
       resp.setRating(eventReviewService.getAverageEventRating(eventId,dateType));
       resp.setTicketQty(ticketService.getTotalTicketInEvent(eventId));
       resp.setSoldTicket(ticketService.getTicketSoldQuantity(eventId,dateType));
       resp.setTickets(ticketData);
       return resp;
    }
}
