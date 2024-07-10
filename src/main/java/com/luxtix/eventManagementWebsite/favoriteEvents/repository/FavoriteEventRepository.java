package com.luxtix.eventManagementWebsite.favoriteEvents.repository;

import com.luxtix.eventManagementWebsite.favoriteEvents.entity.FavoriteEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteEventRepository extends JpaRepository<FavoriteEvents,Long> {

    public static final String eventFavoriteQuery = "SELECT fe FROM FavoriteEvents fe WHERE fe.events.id = :eventId AND fe.users.id = :userId";


    public static final String eventFavoriteCount = "SELECT COUNT(fe.id) FROM FavoriteEvents fe WHERE fe.events.id = :eventId";



    @Query(value = eventFavoriteQuery)
    Optional<FavoriteEvents> getFavoriteEventByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);


    @Query(value = eventFavoriteCount)
    int getFavoriteEventCountByEventId(@Param("eventId") Long eventId);



    @Query("SELECT CASE WHEN (count(fe) > 0) THEN TRUE ELSE FALSE END " +
            "FROM FavoriteEvents fe " +
            "WHERE fe.events.id = :eventId AND fe.users.id = :userId")
    boolean isEventFavorite(@Param("eventId") Long eventId, @Param("userId") Long userId);




}
