package com.luxetix.eventManagementWebsite.favoriteEvents.service.impl;

import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.favoriteEvents.FavoriteEvents;
import com.luxetix.eventManagementWebsite.favoriteEvents.dto.EventToggleDtoResponse;
import com.luxetix.eventManagementWebsite.favoriteEvents.repository.FavoriteEventRepository;
import com.luxetix.eventManagementWebsite.favoriteEvents.service.FavoriteEventService;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteEventServiceImpl implements FavoriteEventService {

    private final FavoriteEventRepository favoriteEventRepository;

    public FavoriteEventServiceImpl(FavoriteEventRepository favoriteEventRepository) {
        this.favoriteEventRepository = favoriteEventRepository;
    }

    @Override
    public EventToggleDtoResponse toggleEvent(long eventId) {
        var claims = Claims.getClaimsFromJwt();
        var userId = (long) claims.get("id");
        Optional<FavoriteEvents> favoriteEventData = favoriteEventRepository.getFavoriteEventByUserIdAndEventId(userId,eventId);
        FavoriteEvents favoriteData = favoriteEventData.orElse(null);
        if(favoriteEventData.isEmpty()){
            FavoriteEvents newFavoriteEvent =  new FavoriteEvents();
            Events event = new Events();
            event.setId(eventId);
            Users user = new Users();
            user.setId(userId);
            newFavoriteEvent.setEvents(event);
            newFavoriteEvent.setUsers(user);
            favoriteEventRepository.save(newFavoriteEvent);
            EventToggleDtoResponse resp = new EventToggleDtoResponse();
            resp.setId(eventId);
            resp.setFavorite(true);
            return resp;
        }else{
            EventToggleDtoResponse resp = new EventToggleDtoResponse();
            resp.setId(eventId);
            resp.setFavorite(false);
            favoriteEventRepository.deleteById(favoriteEventData.get().getId());
            return resp;
        }
    }
}
