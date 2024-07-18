package com.luxtix.eventManagementWebsite.dashboard.dto;


import com.luxtix.eventManagementWebsite.categories.Categories;
import com.luxtix.eventManagementWebsite.city.entity.Cities;
import com.luxtix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class DashboardEventDetailResponseDto {
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
    private List<TicketEventDetailDto> tickets;
    private List<VoucherEventDetailDto> vouchers;



    @Data
    public static class TicketEventDetailDto{

        private Long id;

        private String name;

        private Integer price;

        private Integer qty;
    }

    @Data
    public static class VoucherEventDetailDto{

        private Long id;

        private String name;

        private Integer qty;

        private BigDecimal rate;

        private LocalDate startDate;

        private LocalDate endDate;

        private Boolean referralOnly;
    }


}
