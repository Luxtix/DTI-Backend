package com.luxtix.eventManagementWebsite.events.dto;


import com.luxtix.eventManagementWebsite.categories.Categories;
import com.luxtix.eventManagementWebsite.city.Cities;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class NewEventRequestDto {

    private String name;
    private long category;
    private long city;
    private Boolean isOnline;
    private LocalDate eventDate;
    private String venue;
    private String address;
    private String description;
    private Boolean isPaid;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<TicketEventDto> tickets;
    private List<VoucherEventDto> vouchers;



    @Data
    public static class TicketEventDto{
        private String name;
        private int price;
        private int qty;

    }

    @Data
    public static class VoucherEventDto{
        private String name;
        private int qty;
        private BigDecimal rate;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean referralOnly;

    }

    public Events toEntity() {
        Events event = new Events();
        event.setName(name);
        Categories categories = new Categories();
        categories.setId(category);
        event.setCategories(categories);
        event.setIsOnline(isOnline);
        event.setEventDate(eventDate);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setVenueName(venue);
        event.setAddress(address);
        event.setIsPaid(isPaid);
        Cities newCity = new Cities();
        newCity.setId(city);
        event.setCities(newCity);
        event.setDescriptions(description);
        return event;
    }



}
