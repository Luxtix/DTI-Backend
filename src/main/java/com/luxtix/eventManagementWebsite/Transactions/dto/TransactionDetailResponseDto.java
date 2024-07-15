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
import java.util.List;


@Data
public class TransactionDetailResponseDto {
    private String eventName;
    private String eventImage;
    private String cityName;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String venueName;
    private Boolean isOnline;
    private List<TransactionTicketDto> tickets;

    @Data
    public static class TransactionTicketDto{
        private long Id;
        private String ticketName;
        private int ticketQty;
    }
}
