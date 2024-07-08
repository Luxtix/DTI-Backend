package com.luxtix.eventManagementWebsite.Transactions.dto;


import lombok.Data;

import java.util.List;

@Data
public class TransactionRequestDto {
    private long eventId;
    private Long voucherId;
    private int totalQty;
    private int totalPrice;
    private Integer usePoint;
    private List<TransactionTicketDto> tickets;




    @Data
    public static class TransactionTicketDto{
        private long ticketId;
        private int price;
        private int qty;
    }
}
