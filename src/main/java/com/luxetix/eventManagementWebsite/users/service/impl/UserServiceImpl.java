package com.luxetix.eventManagementWebsite.users.service.impl;


import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.exceptions.InputException;
import com.luxetix.eventManagementWebsite.pointHistory.PointHistory;
import com.luxetix.eventManagementWebsite.pointHistory.repository.PointHistoryRepository;
import com.luxetix.eventManagementWebsite.refferals.entity.Referrals;
import com.luxetix.eventManagementWebsite.refferals.repository.RefferalRepository;
import com.luxetix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxetix.eventManagementWebsite.userUsageRefferals.repository.UserUsageReferralsRepository;
import com.luxetix.eventManagementWebsite.users.dto.ProfileRequestDto;
import com.luxetix.eventManagementWebsite.users.dto.ProfileResponseDto;
import com.luxetix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import com.luxetix.eventManagementWebsite.users.service.UserService;
import com.luxetix.eventManagementWebsite.vouchers.repository.VoucherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserUsageReferralsRepository userUsageReferralsRepository;

    private final VoucherRepository voucherRepository;
    private final CloudinaryService cloudinaryService;

    private final RefferalRepository refferalRepository;

    private final PointHistoryRepository pointHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserUsageReferralsRepository userUsageReferralsRepository, VoucherRepository voucherRepository, CloudinaryService cloudinaryService, RefferalRepository refferalRepository, PointHistoryRepository pointHistoryRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userUsageReferralsRepository = userUsageReferralsRepository;
        this.voucherRepository = voucherRepository;
        this.cloudinaryService = cloudinaryService;
        this.refferalRepository = refferalRepository;
        this.pointHistoryRepository = pointHistoryRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public Users register(UserRegisterRequestDto user) {
        boolean exists = userRepository.findAll()
                .stream()
                .anyMatch(data -> data.getEmail().equals(user.getEmail()));
        if(exists){
            throw new InputException("Email already exist");
        }
        Referrals newRefferals = new Referrals();
        newRefferals.setCode(UUID.randomUUID().toString());
        Referrals data = refferalRepository.save(newRefferals);
        Users newUser = user.toEntity();
        newUser.setReferrals(data);
        userRepository.save(newUser);
        var password = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(password);

        if(!user.getRefferal().equals("")){
            Referrals refferalsData = refferalRepository.findByCode(user.getRefferal()).orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND,"Refferal code with number " + user.getRefferal() + " is not found"));
            UserUsageReferrals userRefferalHistoryData = new UserUsageReferrals();
            userRefferalHistoryData.setUsers(newUser);
            userRefferalHistoryData.setReferrals(refferalsData);
            userUsageReferralsRepository.save(userRefferalHistoryData);
            PointHistory newPointHistory = new PointHistory();
            newPointHistory.setUsers(newUser);
            newPointHistory.setTotalPoint(10000);
            LocalDate today = LocalDate.now();
            LocalDate expirationDate = today.plus(Period.ofMonths(3));
            newPointHistory.setExpirationDate(expirationDate);
            pointHistoryRepository.save(newPointHistory);
        }
        return newUser;
    }

    @Override
    public ProfileResponseDto updateProfile(ProfileRequestDto profileRequestDto) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        if(profileRequestDto.getAvatar() != null){
            userData.setAvatar(cloudinaryService.uploadFile(profileRequestDto.getAvatar(),"folder_1"));
        }
        if(profileRequestDto.getFullname() != null){
            userData.setFullname(profileRequestDto.getFullname());
        }
        if(profileRequestDto.getFullname() != null){
            userData.setFullname(profileRequestDto.getFullname());
        }
        if(profileRequestDto.getPhoneNumber() != null)
        {
            userData.setPhonenumber(profileRequestDto.getPhoneNumber());
        }
       userRepository.save(userData);
        ProfileResponseDto data = new ProfileResponseDto();
        data.setEmail(userData.getEmail());
        data.setFullname(userData.getFullname());
        data.setAvatar(userData.getAvatar());
        data.setPhoneNumber(userData.getPhonenumber());
        data.setFullname(userData.getFullname());
        data.setTotalPoint(userData.getTotalPoints() == null ? 0 : userData.getTotalPoints());
        data.setRefferalCode(userData.getReferrals().getCode());
        return data;
    }

    @Override
    public ProfileResponseDto getProfileData() {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        ProfileResponseDto data = new ProfileResponseDto();
        data.setFullname(userData.getFullname());
        data.setAvatar(userData.getAvatar());
        data.setPhoneNumber(userData.getPhonenumber());
        data.setFullname(userData.getFullname());
        data.setTotalPoint(userData.getTotalPoints() == null ? 0 : userData.getTotalPoints());
        data.setRefferalCode(userData.getReferrals().getCode());
        return data;
    }
}
