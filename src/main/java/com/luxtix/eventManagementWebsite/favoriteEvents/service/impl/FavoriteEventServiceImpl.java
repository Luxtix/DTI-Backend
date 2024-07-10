package com.luxtix.eventManagementWebsite.favoriteEvents.service.impl;

import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.favoriteEvents.entity.FavoriteEvents;
import com.luxtix.eventManagementWebsite.favoriteEvents.dto.EventToggleDtoResponse;
import com.luxtix.eventManagementWebsite.favoriteEvents.repository.FavoriteEventRepository;
import com.luxtix.eventManagementWebsite.favoriteEvents.service.FavoriteEventService;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteEventServiceImpl implements FavoriteEventService {

    private final FavoriteEventRepository favoriteEventRepository;

    public FavoriteEventServiceImpl(FavoriteEventRepository favoriteEventRepository) {
        this.favoriteEventRepository = favoriteEventRepository;
    }

    @Override
    public EventToggleDtoResponse toggleEvent(long eventId, long userId) {
        Optional<FavoriteEvents> favoriteEventData = favoriteEventRepository.getFavoriteEventByUserIdAndEventId(userId,eventId);
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

    @Override
    public int getFavoriteEventCount(long eventId) {
        return favoriteEventRepository.getFavoriteEventCountByEventId(eventId);
    }


    @Override
    public boolean isEventFavorite(long eventId, long userId){
        return favoriteEventRepository.isEventFavorite(eventId,userId);
    }
}
