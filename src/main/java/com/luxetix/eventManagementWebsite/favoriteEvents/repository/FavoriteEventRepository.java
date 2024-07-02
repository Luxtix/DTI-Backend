package com.luxetix.eventManagementWebsite.favoriteEvents.repository;

import com.luxetix.eventManagementWebsite.favoriteEvents.entity.FavoriteEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteEventRepository extends JpaRepository<FavoriteEvents,Long> {

    public static final String eventFavoriteQuery = "SELECT fe FROM FavoriteEvents fe WHERE fe.events.id = :eventId AND fe.users.id = :userId";



    @Query(value = eventFavoriteQuery)
    Optional<FavoriteEvents> getFavoriteEventByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

}
