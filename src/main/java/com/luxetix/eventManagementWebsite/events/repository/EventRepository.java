package com.luxetix.eventManagementWebsite.events.repository;


import com.luxetix.eventManagementWebsite.events.dao.EventListDao;
import com.luxetix.eventManagementWebsite.events.entity.EventType;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Events,Long> {


    public static final String eventWithFilterCategoryAndDateAndTypeAndCityAndNameQuery = "SELECT e.id AS eventId, COALESCE(MIN(t.price), 0) AS ticketPrice, cat.name AS categoryName, cit.name AS cityName, CASE WHEN COALESCE(MIN(t.price), 0) > 0 THEN 'Paid' ELSE 'Free' END AS priceType, e.name AS eventName, e.address as address, e.venueName as venueName, e.eventImage as eventImage, e.descriptions as descriptions, e.startDate as startDate, e.endDate as endDate, e.startTime as startTime, e.endTime as endTime, e.isOnline as isOnline, COUNT(fe.id) AS favoriteCount, (SELECT CASE WHEN COUNT(fe2.id) > 0 THEN true ELSE false END FROM FavoriteEvents fe2 WHERE fe2.events.id = e.id AND fe2.users.id = :userId) AS isFavorite FROM Events e LEFT JOIN e.categories cat LEFT JOIN e.cities cit LEFT JOIN (SELECT t1.events.id AS eventId, MIN(t1.price) AS price FROM Tickets t1 GROUP BY t1.events.id) t ON t.eventId = e.id LEFT JOIN e.favoriteEvents fe WHERE e.deletedAt IS NULL AND e.endDate >= CURRENT_DATE AND (:categoryName IS NULL OR cat.name IN (:categoryName)) AND (:cityName IS NULL OR cit.name = :cityName OR :cityName = '') AND (:eventType IS NULL OR (:eventType = 'Free' AND COALESCE(t.price, 0) <= 0) OR (:eventType = 'Paid' AND COALESCE(t.price, 0) > 0)) AND (:isOnline IS NULL OR e.isOnline = :isOnline) AND (:isFavorite IS NULL OR :isFavorite = false OR (:isFavorite = true AND EXISTS (SELECT 1 FROM FavoriteEvents fe2 WHERE fe2.events.id = e.id AND fe2.users.id = :userId))) AND (:eventName IS NULL OR :eventName = '' OR LOWER(e.name) LIKE LOWER(CONCAT('%', :eventName, '%'))) GROUP BY e.id, cat.name, cit.name, e.name, e.address, e.venueName, e.eventImage, e.descriptions, e.startDate, e.endDate, e.startTime, e.endTime, e.isOnline ORDER BY e.startDate";
    @Query(value = eventWithFilterCategoryAndDateAndTypeAndCityAndNameQuery)
    List<EventListDao> getAllEventWithFilter(@Param("userId") Long id, @Param("categoryName") List<String> categoryName,@Param("eventName") String eventName, @Param("cityName") String cityName, @Param("eventType") String eventType, @Param("isOnline") Boolean isOnline, @Param("isFavorite") Boolean isFavorite, Pageable pageable);

    Optional<Events> findEventsById(Long id);
}
