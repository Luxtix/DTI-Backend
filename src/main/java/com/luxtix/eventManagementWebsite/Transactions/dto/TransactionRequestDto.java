package com.luxtix.eventManagementWebsite.Transactions.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TransactionRequestDto {
    @NotNull
    private long eventId;

    private Long voucherId;

    @NotNull
    @Min(2)
    private int totalQty;

    @NotNull
    private int totalPrice;

    @NotNull
    private Integer usePoint;

    private List<TransactionTicketDto> tickets;




    @Data
    public static class TransactionTicketDto{
        @NotNull
        private long ticketId;

        private int price;

        @NotNull
        @Min(1)
        private int qty;
    }
}
