package com.luxetix.eventManagementWebsite.users.service.impl;


import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.exceptions.InputException;
import com.luxetix.eventManagementWebsite.pointHistory.PointHistory;
import com.luxetix.eventManagementWebsite.pointHistory.repository.PointHistoryRepository;
import com.luxetix.eventManagementWebsite.refferals.entity.Referrals;
import com.luxetix.eventManagementWebsite.refferals.repository.RefferalRepository;
import com.luxetix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxetix.eventManagementWebsite.userUsageRefferals.repository.UserUsageReferralsRepository;
import com.luxetix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import com.luxetix.eventManagementWebsite.users.service.UserService;
import com.luxetix.eventManagementWebsite.vouchers.Vouchers;
import com.luxetix.eventManagementWebsite.vouchers.repository.VoucherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserUsageReferralsRepository userUsageReferralsRepository;

    private final VoucherRepository voucherRepository;

    private final RefferalRepository refferalRepository;

    private final PointHistoryRepository pointHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserUsageReferralsRepository userUsageReferralsRepository, VoucherRepository voucherRepository, RefferalRepository refferalRepository, PointHistoryRepository pointHistoryRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userUsageReferralsRepository = userUsageReferralsRepository;
        this.voucherRepository = voucherRepository;
        this.refferalRepository = refferalRepository;
        this.pointHistoryRepository = pointHistoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users register(UserRegisterRequestDto user) {
        boolean exists = userRepository.findAll()
                .stream()
                .anyMatch(data -> data.getEmail().equals(user.getEmail()));
        if(exists){
            throw new InputException("Email already exist");
        }
        Users newUser = user.toEntity();
        userRepository.save(newUser);
        var password = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(password);
        Referrals newRefferals = new Referrals();
        newRefferals.setCode(UUID.randomUUID().toString());
        newRefferals.setUser(newUser);
        refferalRepository.save(newRefferals);
        if(!user.getRefferal().equals("")){
            Referrals refferalsData = refferalRepository.findByCode(user.getRefferal()).orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND,"Refferal code with number " + user.getRefferal() + " is not found"));
            UserUsageReferrals userRefferalHistoryData = new UserUsageReferrals();
            userRefferalHistoryData.setUsers(newUser);
            userRefferalHistoryData.setReferrals(refferalsData);
            userUsageReferralsRepository.save(userRefferalHistoryData);
            Vouchers newVouchers = new Vouchers();
            newVouchers.setUsers(newUser);
            newVouchers.setRate(new BigDecimal("10.00"));
            newVouchers.setName("Special voucher with refferal code");
            voucherRepository.save(newVouchers);
            PointHistory newPointHistory = new PointHistory();
            newPointHistory.setUsers(refferalsData.getUser());
            newPointHistory.setTotalPoint(10000);
            LocalDate today = LocalDate.now();
            LocalDate expirationDate = today.plus(Period.ofMonths(3));
            newPointHistory.setExpirationDate(expirationDate);
            pointHistoryRepository.save(newPointHistory);
        }
        return newUser;
    }
}
