package com.luxetix.eventManagementWebsite.events.dto;


import com.luxetix.eventManagementWebsite.city.Cities;
import com.luxetix.eventManagementWebsite.events.entity.EventType;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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

    private LocalTime startTime;

    private LocalTime endTime;

    private String venue;

    private String address;

    private String city;

    private String description;

    private MultipartFile image;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private String ticketName;

    private int ticketPrice;

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
        Cities newCity = new Cities();
        newCity.setName(city);
        event.setCities(newCity);
        event.setDescriptions(description);
//        event.setEventImage(image);
        event.setType(type);;
        return event;
    }



}
