package com.luxetix.eventManagementWebsite.Transactions.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class GetTransactionResponseDto {
    private long id;
    private LocalDate eventDate;
    private String eventName;
    private String descriptions;
}
