package com.luxetix.eventManagementWebsite.events.dto;


import com.luxetix.eventManagementWebsite.events.entity.EventType;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class NewEventRequestDto {

    private String name;

    private long category;

    private boolean isOnline;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String venue;

    private String address;

    private String city;

    private String description;

    private String image;

    @Enumerated(EnumType.STRING)
    private Events.EventType type;

    private String ticketName;

    private int tickerPrice;

    private int ticketQty;

    private int voucherQuota;

    public Events toEntity() {
        Events event = new Events();
        event.setName(name);
//        event.setCategories(category);
//        event.setOrganizers(organizer);
        event.setOnline(isOnline);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setVenueName(venue);
        event.setAddress(address);
        event.setCity(city);
        event.setDescriptions(description);
        event.setEventImage(image);
        event.setType(type);;
        return event;
    }



}
