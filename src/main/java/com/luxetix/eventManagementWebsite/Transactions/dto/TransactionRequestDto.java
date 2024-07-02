package com.luxetix.eventManagementWebsite.Transactions.dto;


import com.luxetix.eventManagementWebsite.tickets.dto.TicketDto;
import lombok.Data;

import java.util.List;

@Data
public class TransactionRequestDto {
    private long eventId;
    private Long voucherId;
    private int totalQty;
    private int totalPrice;
    private int usePoint;
    private List<TransactionTicketDto> tickets;




    @Data
    public static class TransactionTicketDto{
        private long ticketId;
        private int price;
        private int qty;
    }
}
