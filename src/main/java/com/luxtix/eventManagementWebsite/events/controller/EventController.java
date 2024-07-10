package com.luxtix.eventManagementWebsite.events.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luxtix.eventManagementWebsite.adapter.LocalDateTypeAdapter;
import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.events.dto.EventDetailDtoResponse;
import com.luxtix.eventManagementWebsite.events.dto.EventListDtoResponse;
import com.luxtix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxtix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.events.services.EventService;
import com.luxtix.eventManagementWebsite.adapter.LocalTimeTypeAdapter;
import com.luxtix.eventManagementWebsite.response.Response;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
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
    @RolesAllowed({"USER"})
    public ResponseEntity<Response<List<EventListDtoResponse>>> getAllEvent(@RequestParam(value = "category",required = false) String category, @RequestParam(value ="city", required = false) String city, @RequestParam(value = "isPaid", required = false) Boolean isPaid, @RequestParam(value = "eventName",required = false) String eventName, @RequestParam(value = "isOnline",required = false) Boolean isOnline, @RequestParam(value = "isFavorite",required = false) Boolean isFavorite, @RequestParam(defaultValue = "0",required = false) int page, @RequestParam(defaultValue = "10",required = false) int size) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        List<EventListDtoResponse> data = eventService.getAllEvent(email,category,city,eventName,isPaid,isOnline,isFavorite, page, size);
        Page<EventListDtoResponse> pageDto = new PageImpl<>(data);
        return Response.successfulResponseWithPage(HttpStatus.OK.value(),"All event fetched successfully", data,pageDto.getTotalPages(),pageDto.getTotalElements(),pageDto.getNumber());

    }

    @GetMapping("/{id}")
    @RolesAllowed({"USER"})
    public ResponseEntity<Response<EventDetailDtoResponse>> getEventById(@PathVariable("id") long id){
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        var isReferrals = (Boolean) claims.get("isReferral");
        return Response.successfulResponse("Event has been fetched successfully", eventService.getEventById(email, isReferrals,id));
    }


    @DeleteMapping("/{id}")
    @RolesAllowed({"ORGANIZER"})
    public ResponseEntity<Response<Events>> deleteEventById(@PathVariable("id") long id){
        eventService.deleteEventById(id);
        return Response.successfulResponse("Event has been deleted successfully");
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
}
