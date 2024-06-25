package com.luxetix.eventManagementWebsite.events.controller;


import com.luxetix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.EventType;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.events.services.EventService;
import com.luxetix.eventManagementWebsite.response.Response;
import com.luxetix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public ResponseEntity<Response<Events>> addNewEvent(@ModelAttribute NewEventRequestDto newEventRequestDto) {
        return Response.successfulResponse("Event registered successfully", eventService.addNewEvent(newEventRequestDto));
    }

    @GetMapping("")
    public ResponseEntity<Response<List<GetEventListDtoResponse>>> getAllEvent(@RequestParam(value = "category",required = false) String category, @RequestParam(value ="city", required = false) String city, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "eventName",required = false) String eventName, @RequestParam(value = "online",required = false) Boolean online,@RequestParam(value = "favorite",required = false) Boolean favorite, @RequestParam(defaultValue = "0",required = false) int page, @RequestParam(defaultValue = "10",required = false) int size) {
        List<String> categoryList = new ArrayList<>();
        if(category!=null){
            categoryList.addAll(Stream.of(category.split(","))
                    .map(String::trim)
                    .toList());
        }

        return Response.successfulResponse("All event fetched successfully", eventService.getAllEvent(categoryList,city,eventName,type,online,favorite, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Events>> getEventById(@PathVariable("id") long id){
        return Response.successfulResponse("Event has been fetched succesfully", eventService.getEventById(id));
    }
}
