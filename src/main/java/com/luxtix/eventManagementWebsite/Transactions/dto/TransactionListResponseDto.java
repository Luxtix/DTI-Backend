package com.luxtix.eventManagementWebsite.Transactions.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionListResponseDto {
    private long transactionId;
    private long eventId;
    private LocalDate eventDate;
    private String eventName;
    private String eventImage;
    private boolean isDone;
}
