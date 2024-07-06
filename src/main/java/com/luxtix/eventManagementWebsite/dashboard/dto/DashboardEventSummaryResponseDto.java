package com.luxtix.eventManagementWebsite.dashboard.dto;


import com.luxtix.eventManagementWebsite.eventReviews.dto.EventReviewsDto;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class DashboardEventSummaryResponseDto {
    private String name;
    private String venue;
    private String address;
    private String city;
    private LocalDate eventDate;
    private String eventDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private int ticketQty;
    private int remainingTicket;
    private int revenue;
    private double rating;
    private List<TicketSummaryDao> tickets;
//    private List<EventReviewsDto> reviews;

}
