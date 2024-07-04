package com.luxtix.eventManagementWebsite.userUsageRefferals.service;

import com.luxtix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;

import java.time.Instant;

public interface UserUsageReferralsService {
    boolean checkUserIsReferralAndValid(long userId, Instant expiredDate);

    UserUsageReferrals getUserUsageReferralData(long userId);

    void addNewUserUsageReferralData(UserUsageReferrals userUsageReferrals);
}
