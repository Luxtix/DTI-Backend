package com.luxtix.eventManagementWebsite.Transactions.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetTransactionResponseDto {
    private long id;
    private String eventName;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String eventDay;
    private String venue;
    private String ticketName;
    private int ticketQty;
    private String cityName;
    private boolean isOnline;
}
