package com.luxtix.eventManagementWebsite.favoriteEvents.controller;
import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.favoriteEvents.dto.EventToggleDtoResponse;
import com.luxtix.eventManagementWebsite.favoriteEvents.service.FavoriteEventService;
import com.luxtix.eventManagementWebsite.response.Response;
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
        var claims = Claims.getClaimsFromJwt();
        var userId = (long) claims.get("id");
        return Response.successfulResponse("Event toggle successfully", favoriteEventService.toggleEvent(id,userId));
    }
}
