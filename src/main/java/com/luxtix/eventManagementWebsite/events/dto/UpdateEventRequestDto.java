package com.luxtix.eventManagementWebsite.events.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;



@Data
public class UpdateEventRequestDto {
    private String name;
    private Long category;
    private Long city;
    private Boolean isOnline;
    private LocalDate eventDate;
    private String venue;
    private String address;
    private String description;
    private Boolean isPaid;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<TicketEventUpdateDto> tickets;
    private List<VoucherEventUpdateDto> vouchers;



    @Data
    public static class TicketEventUpdateDto{
        private Long id;
        private String name;
        private Integer price;
        private Integer qty;
    }

    @Data
    public static class VoucherEventUpdateDto{
        private Long id;
        private String name;
        private int qty;
        private BigDecimal rate;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean referralOnly;
    }
}
