package com.luxetix.eventManagementWebsite.userUsageRefferals.service;

import com.luxetix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;

import java.time.Instant;

public interface UserUsageReferralsService {
    boolean checkUserIsReferralAndValid(long userId, Instant expiredDate);

    UserUsageReferrals getUserUsageReferralData(long userId);

    void addNewUserUsageReferralData(UserUsageReferrals userUsageReferrals);
}
