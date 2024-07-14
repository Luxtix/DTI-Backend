package com.luxtix.eventManagementWebsite.users.repository;

import com.luxtix.eventManagementWebsite.referrals.entity.Referrals;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);


    Optional<Users> findByReferrals(Referrals referrals);
}
