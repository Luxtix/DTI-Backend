package com.luxetix.eventManagementWebsite.pointHistory.repository;

import com.luxetix.eventManagementWebsite.pointHistory.entity.PointHistory;
import com.luxetix.eventManagementWebsite.pointHistory.dao.PointHistoryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;


@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory,Long> {


    public static final String userPointQuery = "SELECT SUM(ph.totalPoint) as points FROM PointHistory ph WHERE ph.users.id = :userId AND ph.createdAt >= :expiredDate";
    @Query(value = userPointQuery )
    Optional<PointHistoryDao> getUserPoint(@Param("userId") long userId, @Param("expiredDate")Instant expiredDate);
}