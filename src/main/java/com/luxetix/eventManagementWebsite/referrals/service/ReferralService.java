package com.luxetix.eventManagementWebsite.referrals.service;

import com.luxetix.eventManagementWebsite.referrals.entity.Referrals;

public interface ReferralService {

    Referrals addNewReferralCode(Referrals referrals);


    Referrals findByReferralCode(String code);
}
