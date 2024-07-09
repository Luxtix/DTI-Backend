package com.luxtix.eventManagementWebsite.tickets.dto;


import lombok.Data;

@Data
public class TicketDto {
    private long id;
    private String name;
    private Integer price;
    private Integer qty;
    private Integer remainingQty;
}
