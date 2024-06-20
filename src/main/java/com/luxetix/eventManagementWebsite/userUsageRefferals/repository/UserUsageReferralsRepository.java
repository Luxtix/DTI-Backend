package com.luxetix.eventManagementWebsite.userUsageRefferals.repository;

import com.luxetix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserUsageReferralsRepository extends JpaRepository<UserUsageReferrals,Long> {
}
