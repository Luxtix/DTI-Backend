package com.luxtix.eventManagementWebsite.referrals.service;

import com.luxtix.eventManagementWebsite.referrals.entity.Referrals;

public interface ReferralService {

    Referrals addNewReferralCode(Referrals referrals);


    Referrals findByReferralCode(String code);
}
