package com.luxetix.eventManagementWebsite.tickets.dto;


import lombok.Data;

@Data
public class TicketDto {
    private long id;
    private String name;
    private int price;
    private int qty;
    private int remainingQty;
}
