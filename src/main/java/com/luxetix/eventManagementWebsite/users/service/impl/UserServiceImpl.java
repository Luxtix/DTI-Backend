package com.luxetix.eventManagementWebsite.users.service.impl;


import com.fasterxml.jackson.databind.exc.MismatchedInputException;
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
import com.luxetix.eventManagementWebsite.users.dto.*;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import com.luxetix.eventManagementWebsite.users.service.UserService;
import com.luxetix.eventManagementWebsite.vouchers.repository.VoucherRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserUsageReferralsRepository userUsageReferralsRepository;

    private final CloudinaryService cloudinaryService;

    private final RefferalRepository refferalRepository;

    private final PointHistoryRepository pointHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 12;

    public UserServiceImpl(UserRepository userRepository, UserUsageReferralsRepository userUsageReferralsRepository, VoucherRepository voucherRepository, CloudinaryService cloudinaryService, RefferalRepository refferalRepository, PointHistoryRepository pointHistoryRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userUsageReferralsRepository = userUsageReferralsRepository;
        this.cloudinaryService = cloudinaryService;
        this.refferalRepository = refferalRepository;
        this.pointHistoryRepository = pointHistoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static String generateUniqueId() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }


    @Transactional
    public Users register(UserRegisterRequestDto user) {
        boolean exists = userRepository.findAll()
                .stream()
                .anyMatch(data -> data.getEmail().equals(user.getEmail()));
        if(exists){
            throw new InputException("Email already exist");
        }
        Referrals newReferral = new Referrals();
        String uniqueReferral = generateUniqueId();
        newReferral.setCode(uniqueReferral);
        Referrals data = refferalRepository.save(newReferral);
        Users newUser = user.toEntity();
        newUser.setReferrals(data);
        userRepository.save(newUser);
        var password = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(password);

        if(!user.getReferral().equals("")){
            Referrals refferalsData = refferalRepository.findByCode(user.getReferral()).orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND,"Referral code with number " + user.getReferral() + " is not found"));
            UserUsageReferrals userRefferalHistoryData = new UserUsageReferrals();
            userRefferalHistoryData.setUsers(newUser);
            userRefferalHistoryData.setReferrals(refferalsData);
            userUsageReferralsRepository.save(userRefferalHistoryData);
            PointHistory newPointHistory = new PointHistory();
            newPointHistory.setUsers(newUser);
            newPointHistory.setTotalPoint(10000);
            pointHistoryRepository.save(newPointHistory);
        }
        return newUser;
    }

    @Override
    @CachePut(value = "getProfileData",key = "#email")
    public ProfileResponseDto updateProfile(String email,ProfileRequestDto profileRequestDto) {
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        if(profileRequestDto.getAvatar() != null){
            userData.setAvatar(cloudinaryService.uploadFile(profileRequestDto.getAvatar(),"folder_luxtix"));
        }
        if(profileRequestDto.getFullname() != null){
            userData.setFullname(profileRequestDto.getFullname());
        }
        if(profileRequestDto.getPhoneNumber() != null)
        {
            userData.setPhoneNumber(profileRequestDto.getPhoneNumber());
        }
        userRepository.save(userData);
        ProfileResponseDto data = new ProfileResponseDto();
        data.setEmail(userData.getEmail());
        data.setFullname(userData.getFullname());
        data.setAvatar(userData.getAvatar());
        data.setPhoneNumber(userData.getPhoneNumber());
        data.setFullname(userData.getFullname());
        data.setRefferalCode(userData.getReferrals().getCode());
        return data;
    }

    @Override
    @Cacheable(value = "getProfileData",key = "#email")
    public ProfileResponseDto getProfileData(String email) {
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        ProfileResponseDto data = new ProfileResponseDto();
        data.setEmail(email);
        data.setFullname(userData.getFullname());
        data.setAvatar(userData.getAvatar());
        data.setPhoneNumber(userData.getPhoneNumber());
        data.setFullname(userData.getFullname());
        data.setRefferalCode(userData.getReferrals().getCode());
        return data;
    }

    @Override
    public ChangePasswordResponseDto changePassword(ChangePasswordRequestDto data,String email) {
        if(!data.getNewPassword().equals(data.getConfirmPassword())){
            throw new InputException("Password and confirm password doesn't match");
        }
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("You are not logged in yet"));
        if(!passwordEncoder.matches(data.getOldPassword(), userData.getPassword())){
            throw new InputException("Old password is wrong");
        }
        var password = passwordEncoder.encode(data.getNewPassword());
        userData.setPassword(password);
        userRepository.save(userData);
        ChangePasswordResponseDto resp = new ChangePasswordResponseDto();
        resp.setEmail(email);
        resp.setPassword(password);
        return resp;
    }
}
