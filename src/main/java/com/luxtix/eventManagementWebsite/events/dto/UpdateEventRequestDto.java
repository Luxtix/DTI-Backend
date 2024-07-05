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

//    public Events toEntity() {
//        Events event = new Events();
//        if(!name.isEmpty()){
//            event.setName(name);
//        }
//
//        if(category != 0){
//            Categories categories = new Categories();
//            categories.setId(category);
//            event.setCategories(categories);
//        }
//        if(isOnline != null){
//            event.setIsOnline(isOnline);
//        }
//        if(eventDate != null){
//            event.setEventDate(eventDate);
//        }
//        if(startTime != null){
//            event.setStartTime(startTime);
//        }
//        if(endTime != null){
//            event.setEndTime(endTime);
//        }
//        if(venue.isEmpty()){
//            event.setVenueName(venue);
//        }
//        if(address.isEmpty()){
//            event.setAddress(address);
//        }
//        if(isPaid != null){
//            event.setIsPaid(isPaid);
//        }
//        if(city != 0){
//            Cities newCity = new Cities();
//            newCity.setId(city);
//            event.setCities(newCity);
//        }
//        if(description.isEmpty()){
//            event.setDescriptions(description);
//        }
//        return event;
//    }

}
