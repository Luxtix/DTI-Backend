package com.luxtix.eventManagementWebsite.events.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventListDtoResponse {
    private long id;
    private String EventName;
    private String address;
    private String venueName;
    private String eventImage;
    private String descriptions;
    private int ticketPrice;
    private String priceCategory;
    private String categoryName;
    private String cityName;
    private Boolean isOnline;
    private int favoriteCount;
    private Boolean isFavorite;
    private LocalDate eventDate;
    private String eventDay;
    private LocalTime startTime;
    private LocalTime endTime;

}
