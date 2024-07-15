package com.luxtix.eventManagementWebsite.Transactions.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TransactionRequestDto {
    @NotNull
    private long eventId;

    private Long voucherId;

    @NotNull
    @Min(1)
    private int totalQty;

    @NotNull
    private BigDecimal finalPrice;

    @NotNull
    private BigDecimal totalDiscount;

    @NotNull
    private BigDecimal originalPrice;

    @NotNull
    private Integer usePoint;

    private List<TransactionTicketDto> tickets;




    @Data
    public static class TransactionTicketDto{
        @NotNull(message = "Ticket ID is required")
        private Long ticketId;

        @Min(value = 0, message = "Price must be at least 0")
        private int price;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private int qty;
    }
}
