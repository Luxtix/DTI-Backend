package com.luxtix.eventManagementWebsite.events.repository;


import com.luxtix.eventManagementWebsite.events.dao.EventDetailDao;
import com.luxtix.eventManagementWebsite.events.dao.EventListDao;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Events,Long> {


    public static final String eventWithFilterCategoryAndDateAndTypeAndCityAndNameQuery = "SELECT e.id AS eventId, COALESCE(MIN(t.price), 0) AS ticketPrice, cat.name AS categoryName, cit.name AS cityName, CASE WHEN e.isPaid THEN 'Paid' ELSE 'Free' END AS priceCategory, e.name AS eventName, e.address as address, e.venueName as venueName, e.eventImage as eventImage, e.descriptions as descriptions, e.eventDate as eventDate, e.startTime as startTime, e.endTime as endTime, e.isOnline as isOnline, COUNT(fe.id) AS favoriteCount, (SELECT CASE WHEN COUNT(fe2.id) > 0 THEN true ELSE false END FROM FavoriteEvents fe2 WHERE fe2.events.id = e.id AND fe2.users.id = :userId) AS isFavorite FROM Events e LEFT JOIN e.categories cat LEFT JOIN e.cities cit LEFT JOIN (SELECT t1.events.id AS eventId, MIN(t1.price) AS price FROM Tickets t1 GROUP BY t1.events.id) t ON t.eventId = e.id LEFT JOIN e.favoriteEvents fe WHERE e.deletedAt IS NULL AND e.eventDate >= CURRENT_DATE AND (:categoryName IS NULL OR  :categoryName = '' OR cat.name = :categoryName) AND (:cityName IS NULL OR cit.name = :cityName OR :cityName = '') AND (:eventType IS NULL OR (:eventType = false AND NOT e.isPaid) OR (:eventType = true AND e.isPaid)) AND (:isOnline IS NULL OR e.isOnline = :isOnline) AND (:isFavorite IS NULL OR :isFavorite = false OR (:isFavorite = true AND EXISTS (SELECT 1 FROM FavoriteEvents fe2 WHERE fe2.events.id = e.id AND fe2.users.id = :userId))) AND (:eventName IS NULL OR :eventName = '' OR LOWER(e.name) LIKE LOWER(CONCAT('%', :eventName, '%'))) GROUP BY e.id, cat.name, cit.name, e.name, e.address, e.venueName, e.eventImage, e.descriptions, e.eventDate, e.startTime, e.endTime, e.isOnline ORDER BY e.eventDate";


    public static final String eventDetailsQuery = "SELECT e.id AS eventId, e.name AS eventName, e.cities.name AS cityName, e.categories.name as categoryName, e.address AS address, e.eventImage AS eventImage, e.venueName AS venueName, e.descriptions AS description, e.eventDate AS eventDate, e.startTime AS startTime, e.endTime AS endTime, e.isOnline AS isOnline, CASE WHEN e.isPaid THEN 'Paid' ELSE 'Free' END AS priceCategory, u.fullname AS organizerName, u.avatar AS organizerAvatar, (SELECT COUNT(fe.id) FROM FavoriteEvents fe WHERE fe.events.id = e.id) AS favoriteCounts, (CASE WHEN EXISTS (SELECT 1 FROM FavoriteEvents fe2 WHERE fe2.events.id = e.id AND fe2.users.id = :userId) THEN TRUE ELSE FALSE END) AS isFavorite, CASE WHEN e.eventDate < CURRENT_DATE() THEN TRUE ELSE FALSE END AS isDone FROM Events e JOIN e.users u  WHERE e.id = :eventId GROUP BY e.id, u.id,e.categories.name, e.cities.name";

    @Query(value = eventWithFilterCategoryAndDateAndTypeAndCityAndNameQuery)
    Page<EventListDao> getAllEventWithFilter(@Param("userId") Long id, @Param("categoryName") String categoryName, @Param("eventName") String eventName, @Param("cityName") String cityName, @Param("eventType") Boolean eventType, @Param("isOnline") Boolean isOnline, @Param("isFavorite") Boolean isFavorite, Pageable pageable);


    @Query(value = eventDetailsQuery)
    EventDetailDao getEventById(@Param("userId") Long userId, @Param("eventId") Long eventId);
}
