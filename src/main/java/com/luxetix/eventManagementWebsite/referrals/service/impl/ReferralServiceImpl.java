package com.luxetix.eventManagementWebsite.referrals.service.impl;


import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.referrals.entity.Referrals;
import com.luxetix.eventManagementWebsite.referrals.repository.ReferralRepository;
import com.luxetix.eventManagementWebsite.referrals.service.ReferralService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ReferralServiceImpl implements ReferralService {

    private final ReferralRepository referralRepository;

    public ReferralServiceImpl(ReferralRepository referralRepository) {
        this.referralRepository = referralRepository;
    }

    @Override
    public Referrals addNewReferralCode(Referrals referrals) {
        return referralRepository.save(referrals);
    }

    @Override
    public Referrals findByReferralCode(String code) {
        return referralRepository.findByCode(code).orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND,"Referral code with number " + code + " is not found"));
    }
}
