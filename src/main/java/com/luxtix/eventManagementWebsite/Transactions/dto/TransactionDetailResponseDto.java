package com.luxtix.eventManagementWebsite.Transactions.dto;


import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;


@Data
public class TransactionDetailResponseDto {
    private long id;
    private String eventName;
    private String eventImage;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String eventDay;
    private String venue;
    private String ticketName;
    private int ticketQty;
    private String cityName;
    private Boolean isOnline;
}
