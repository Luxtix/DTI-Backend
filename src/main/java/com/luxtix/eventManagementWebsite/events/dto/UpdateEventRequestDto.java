package com.luxtix.eventManagementWebsite.events.dto;

import com.luxtix.eventManagementWebsite.categories.Categories;
import com.luxtix.eventManagementWebsite.city.entity.Cities;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;



@Data
public class UpdateEventRequestDto {

    @NotNull(message = "Event name is required")
    private String name;

    @NotNull(message = "Category id is required")
    private Long category;

    @NotNull(message = "City id is required")
    private Long city;

    @NotNull(message = "isOnline is required")
    private Boolean isOnline;

    @NotNull(message = "Event date is required")
    private LocalDate eventDate;

    @NotNull(message = "Event venue is required")
    private String venue;

    @NotNull(message = "Event address is required")
    private String address;

    @NotNull(message = "Event description is required")
    private String description;

    @NotNull(message = "isPaid is required")
    private Boolean isPaid;

    @NotNull(message = "Event start time is required")
    private LocalTime startTime;

    @NotNull(message = "Event end time is required")
    private LocalTime endTime;
    private List<TicketEventUpdateDto> tickets;
    private List<VoucherEventUpdateDto> vouchers;



    @Data
    public static class TicketEventUpdateDto{

        @NotNull(message = "Ticket ID is required")
        private Long id;

        @NotNull(message = "Ticket name is required")
        private String name;

        @NotNull(message = "Ticket price is required")
        private Integer price;

        @NotNull(message = "Ticket quantity is required")
        @Min(value = 1, message = "Total quantity must be at least 1")
        private Integer qty;


        public Tickets toEntity(){
            Tickets ticket = new Tickets();
            ticket.setId(id);
            ticket.setName(name);
            ticket.setPrice(price);
            ticket.setQty(qty);
            return ticket;
        }
    }

    @Data
    public static class VoucherEventUpdateDto{

        @NotNull(message = "Voucher ID is required")
        private Long id;

        @NotNull(message = "Voucher name is required")
        private String name;


        @NotNull(message = "Voucher quantity is required")
        @Min(value = 1, message = "Total quantity must be at least 1")
        private Integer qty;


        @NotNull(message = "Rate is required")
        @DecimalMin(value = "0.01", message = "Rate must be at least 0.01")
        private BigDecimal rate;

        @NotNull(message = "Voucher start date is required")
        private LocalDate startDate;

        @NotNull(message = "Voucher end date is required")
        private LocalDate endDate;

        @NotNull(message = "Voucher referral only is required")
        private Boolean referralOnly;


        public Vouchers toEntity(){
            Vouchers voucher = new Vouchers();
            voucher.setId(id);
            voucher.setReferralOnly(referralOnly);
            voucher.setName(name);
            voucher.setVoucherLimit(qty);
            voucher.setRate(rate);
            voucher.setStartDate(startDate);
            voucher.setEndDate(endDate);

            return voucher;
        }
    }


    public Events toEntity() {
        Events event = new Events();
        event.setName(name);
        Categories categories = new Categories();
        categories.setId(category);
        event.setCategories(categories);
        event.setIsOnline(isOnline);
        event.setEventDate(eventDate);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setVenueName(venue);
        event.setAddress(address);
        event.setIsPaid(isPaid);
        Cities newCity = new Cities();
        newCity.setId(city);
        event.setCities(newCity);
        event.setDescriptions(description);
        return event;
    }
}
