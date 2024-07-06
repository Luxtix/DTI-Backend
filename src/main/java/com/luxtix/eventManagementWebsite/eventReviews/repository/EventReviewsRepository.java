package com.luxtix.eventManagementWebsite.eventReviews.repository;

import com.luxtix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxtix.eventManagementWebsite.eventReviews.entity.EventReviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Repository
public interface EventReviewsRepository extends JpaRepository<EventReviews,Long> {



//    public static final String eventReviewsDetailQuery = "SELECT er.id as id, er.reviewCategory as reviewCategory, er.rating as rating, er.comment as comment, er.users.fullname as reviewerName from EventReviews er WHERE er.events.id = :eventId";
//
////    @Query(value = eventReviewsDetailQuery)
    Optional<Page<EventReviews>> findByEventsId(long eventId, Pageable pageable);


}
