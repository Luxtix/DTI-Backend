package com.luxtix.eventManagementWebsite.userUsageRefferals.service.impl;

import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxtix.eventManagementWebsite.userUsageRefferals.repository.UserUsageReferralsRepository;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class UserUsageReferralsServiceImpl implements UserUsageReferralsService {

    private final UserUsageReferralsRepository userUsageReferralsRepository;

    public UserUsageReferralsServiceImpl(UserUsageReferralsRepository userUsageReferralsRepository) {
        this.userUsageReferralsRepository = userUsageReferralsRepository;
    }

    @Override
    public boolean checkUserIsReferralAndValid(long userId, Instant expiredDate) {
        return userUsageReferralsRepository.checkUserIsReferralAndValid(userId,expiredDate);
    }

    @Override
    public UserUsageReferrals getUserUsageReferralData(long userId) {
        return userUsageReferralsRepository.findByUsersId(userId).orElseThrow(() -> new DataNotFoundException("User usage history with user id " + userId + " is not found"));
    }

    @Override
    public void addNewUserUsageReferralData(UserUsageReferrals userUsageReferrals) {
        userUsageReferralsRepository.save(userUsageReferrals);
    }
}
