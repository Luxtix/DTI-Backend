package com.luxetix.eventManagementWebsite.refferals.repository;

import com.luxetix.eventManagementWebsite.refferals.entity.Referrals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefferalRepository extends JpaRepository<Referrals,Long> {
    Optional<Referrals> findByCode(String code);
}
