package com.luxetix.eventManagementWebsite.eventReviews.repository;

import com.luxetix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxetix.eventManagementWebsite.events.dao.EventListDao;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventReviewsRepository extends JpaRepository<Events,Long> {



    public static final String eventReviewsDetailQuery = "SELECT er.id as id, er.reviewCategory as reviewCategory, er.rating as rating, er.comment as comment, er.users.fullname as reviewerName from EventReviews er WHERE er.events.id = :eventId";

    @Query(value = eventReviewsDetailQuery)
    List<EventReviewsDao> getEventReviews(@Param("eventId") long eventId);


}
