package com.luxtix.eventManagementWebsite.events.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetEventListDtoResponse {
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
    private boolean isOnline;
    private int favoriteCount;
    private boolean isFavorite;
    private LocalDate eventDate;
    private String eventDay;
    private LocalTime startTime;
    private LocalTime endTime;

}
