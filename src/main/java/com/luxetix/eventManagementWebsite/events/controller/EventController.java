package com.luxetix.eventManagementWebsite.events.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luxetix.eventManagementWebsite.adapter.LocalDateTypeAdapter;
import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.eventReviews.dto.ReviewEventRequestDto;
import com.luxetix.eventManagementWebsite.eventReviews.dto.ReviewEventResponseDto;
import com.luxetix.eventManagementWebsite.events.dao.EventListDao;
import com.luxetix.eventManagementWebsite.events.dto.EventDetailDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.events.services.EventService;
import com.luxetix.eventManagementWebsite.adapter.LocalTimeTypeAdapter;
import com.luxetix.eventManagementWebsite.response.Response;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@Validated
@Log
public class EventController {

    private final EventService eventService;


    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("")
    @RolesAllowed({"ORGANIZER"})
    public ResponseEntity<Response<Events>> addNewEvent(@RequestParam("image") MultipartFile image,  @RequestParam("eventData") String eventData) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter());
        Gson gson = gsonBuilder.create();
        NewEventRequestDto data = gson.fromJson(eventData, NewEventRequestDto.class);
        if(image.isEmpty()){
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(),"Image is empty");
        }
        String fileName = image.getOriginalFilename();
        if(!eventService.isValidImageExtension(fileName)){
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(),"Invalid image type");
        }
        return Response.successfulResponse("Event registered successfully",eventService.addNewEvent(image,data,email));
    }

    @GetMapping("")
    public ResponseEntity<Response<List<GetEventListDtoResponse>>> getAllEvent(@RequestParam(value = "category",required = false) String category, @RequestParam(value ="city", required = false) String city, @RequestParam(value = "isPaid", required = false) Boolean isPaid, @RequestParam(value = "eventName",required = false) String eventName, @RequestParam(value = "isOnline",required = false) Boolean isOnline,@RequestParam(value = "isFavorite",required = false) Boolean isFavorite, @RequestParam(defaultValue = "0",required = false) int page, @RequestParam(defaultValue = "10",required = false) int size) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        Page<EventListDao> data = eventService.getAllEvent(email,category,city,eventName,isPaid,isOnline,isFavorite, page, size);
        List<GetEventListDtoResponse> eventList = eventService.convertAllEventToDto(data);
        return Response.successfulResponseWithPage(HttpStatus.OK.value(), "All event fetched successfully",eventList,data.getTotalPages(),data.getTotalElements(),data.getNumber());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<EventDetailDtoResponse>> getEventById(@PathVariable("id") long id){
        return Response.successfulResponse("Event has been fetched succesfully", eventService.getEventById(id));
    }


    @DeleteMapping("/{id}")
    @RolesAllowed({"ORGANIZER"})
    public ResponseEntity<Response<Events>> deleteEventById(@PathVariable("id") long id){
        eventService.deleteEventById(id);
        return Response.successfulResponse("Event has been delete successfully");
    }

    @PutMapping("/{id}")
    @RolesAllowed({"ORGANIZER"})
    public ResponseEntity<Response<Events>> updateEventById(@PathVariable("id") long id,@RequestParam("image") MultipartFile image,  @RequestParam("eventData") String eventData){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter());
        Gson gson = gsonBuilder.create();
        UpdateEventRequestDto data = gson.fromJson(eventData, UpdateEventRequestDto.class);
        return Response.successfulResponse("Event has been update successfully", eventService.updateEvent(id, image, data));
    }

    @PostMapping("/review")
    @RolesAllowed({"USER"})
    public ResponseEntity<Response<ReviewEventResponseDto>> addNewReview(@RequestBody ReviewEventRequestDto data) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("Event registered successfully",eventService.addReview(email,data));
    }



}
