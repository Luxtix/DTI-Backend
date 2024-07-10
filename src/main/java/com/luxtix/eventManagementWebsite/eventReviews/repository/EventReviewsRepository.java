package com.luxtix.eventManagementWebsite.eventReviews.repository;

import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;


@Repository
public interface EventReviewsRepository extends JpaRepository<EventReviews,Long> {

    Optional<Page<EventReviews>> findByEventsId(long eventId, Pageable pageable);


    public static final String averageEventRatingQuery = "SELECT COALESCE(AVG(r.rating), 0) FROM EventReviews r WHERE r.events.id = :eventId AND DATE_TRUNC(:dateFilter, r.createdAt) = DATE_TRUNC(:dateFilter, CURRENT_TIMESTAMP)";


    @Query(value = averageEventRatingQuery)
    BigDecimal getEventAverageRating(long eventId,String dateFilter);
}
