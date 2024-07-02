package com.luxetix.eventManagementWebsite.events.dto;

import com.luxetix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxetix.eventManagementWebsite.eventReviews.dto.EventReviewsDto;
import com.luxetix.eventManagementWebsite.tickets.dto.TicketDto;
import com.luxetix.eventManagementWebsite.vouchers.dto.VoucherDto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Data
public class EventDetailDtoResponse {
    private Long id;
    private String eventName;
    private String cityName;
    private String address;
    private String eventImage;
    private String venueName;
    private String description;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isOnline;
    private Boolean isDone;
    private String priceCategory;
    private String organizerName;
    private String organizerAvatar;
    private Integer favoriteCounts;
    private Boolean isFavorite;
    private List<TicketDto> tickets;
    private List<VoucherDto> vouchers;
    private List<EventReviewsDao> reviews;
}
