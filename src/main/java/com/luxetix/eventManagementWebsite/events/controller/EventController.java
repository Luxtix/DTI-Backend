package com.luxetix.eventManagementWebsite.events.controller;


import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.events.services.EventService;
import com.luxetix.eventManagementWebsite.response.Response;
import com.luxetix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Response<Events>> addNewEvent(@RequestBody NewEventRequestDto newEventRequestDto) {
        return Response.successfulResponse("User registered successfully", eventService.addNewEvent(newEventRequestDto));
    }
}
