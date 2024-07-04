package com.luxtix.eventManagementWebsite.userUsageRefferals.repository;

import com.luxtix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface UserUsageReferralsRepository extends JpaRepository<UserUsageReferrals,Long> {



    public static final String checkUserIsReferralAndValidQuery = "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserUsageReferrals u WHERE u.users.id = :userId AND u.benefitClaim = false and u.createdAt >= :expiredDate";


    @Query(value = checkUserIsReferralAndValidQuery)
    boolean checkUserIsReferralAndValid(@Param("userId") long userId,@Param("expiredDate")Instant expiredDate);

    Optional<UserUsageReferrals> findByUsersId(long userId);
}
