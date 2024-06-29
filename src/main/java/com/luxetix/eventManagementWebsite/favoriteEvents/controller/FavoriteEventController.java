package com.luxetix.eventManagementWebsite.favoriteEvents.controller;
import com.luxetix.eventManagementWebsite.favoriteEvents.dto.EventToggleDtoResponse;
import com.luxetix.eventManagementWebsite.favoriteEvents.service.FavoriteEventService;
import com.luxetix.eventManagementWebsite.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorite/")
public class FavoriteEventController {

    private final FavoriteEventService favoriteEventService;

    public FavoriteEventController(FavoriteEventService favoriteEventService) {
        this.favoriteEventService = favoriteEventService;
    }


    @PostMapping("toggle/{id}")
    public ResponseEntity<Response<EventToggleDtoResponse>> toggleEvent(@PathVariable long id) {
        return Response.successfulResponse("Event toggle successfully", favoriteEventService.toggleEvent(id));
    }
}
