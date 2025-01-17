package com.luxtix.eventManagementWebsite.referrals.repository;

import com.luxtix.eventManagementWebsite.referrals.entity.Referrals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReferralRepository extends JpaRepository<Referrals,Long> {
    Optional<Referrals> findByCode(String code);
}
