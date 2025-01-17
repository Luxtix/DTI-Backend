package com.luxtix.eventManagementWebsite.users.service.impl;


import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.exceptions.InputException;
import com.luxtix.eventManagementWebsite.pointHistory.entity.PointHistory;
import com.luxtix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxtix.eventManagementWebsite.referrals.entity.Referrals;
import com.luxtix.eventManagementWebsite.referrals.service.ReferralService;
import com.luxtix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import com.luxtix.eventManagementWebsite.users.dto.*;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.repository.UserRepository;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;

@Log
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserUsageReferralsService userUsageReferralsService;

    private final CloudinaryService cloudinaryService;

    private final ReferralService referralService;

    private final PointHistoryService pointHistoryService;
    private final PasswordEncoder passwordEncoder;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 12;

    public UserServiceImpl(UserRepository userRepository, UserUsageReferralsService userUsageReferralsService, CloudinaryService cloudinaryService, ReferralService referralService, @Lazy PointHistoryService pointHistoryService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userUsageReferralsService = userUsageReferralsService;
        this.cloudinaryService = cloudinaryService;
        this.referralService = referralService;
        this.pointHistoryService = pointHistoryService;
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
    @Override
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
        Referrals data = referralService.addNewReferralCode(newReferral);
        Users newUser = user.toEntity();
        newUser.setReferrals(data);
        userRepository.save(newUser);
        var password = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(password);

        if(!user.getReferral().isEmpty()){
            Referrals referralsData = referralService.findByReferralCode(user.getReferral());
            UserUsageReferrals userReferralHistoryData = new UserUsageReferrals();
            userReferralHistoryData.setUsers(newUser);
            userReferralHistoryData.setReferrals(referralsData);

            Users referralUser = userRepository.findByReferrals(referralsData).orElseThrow(() -> new DataNotFoundException("User with referral used is not found"));
            userUsageReferralsService.addNewUserUsageReferralData(userReferralHistoryData);
            PointHistory newPointHistory = new PointHistory();
            newPointHistory.setUsers(referralUser);
            newPointHistory.setTotalPoint(10000);
            pointHistoryService.addNewPointHistory(newPointHistory);
        }
        return newUser;
    }

    @Override
    @CachePut(value = "getProfileData",key = "#email")
    public ProfileResponseDto updateProfile(String email, ProfileRequestDto profileRequestDto) {
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        if(profileRequestDto.getAvatar() != null){
            if(userData.getAvatar() != null)
            {
                try {
                    cloudinaryService.deleteImage(userData.getAvatar());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            userData.setAvatar(cloudinaryService.uploadFile(profileRequestDto.getAvatar(),"folder_luxtix"));
        }
        if(profileRequestDto.getDisplayName() != null){
            userData.setFullname(profileRequestDto.getDisplayName());
        }
        if(profileRequestDto.getPhoneNumber() != null)
        {
            userData.setPhoneNumber(profileRequestDto.getPhoneNumber());
        }
        userRepository.save(userData);
        ProfileResponseDto data = new ProfileResponseDto();
        data.setEmail(userData.getEmail());
        data.setDisplayName(userData.getFullname());
        System.out.println(userData.getAvatar());
        data.setAvatar(cloudinaryService.generateUrl(userData.getAvatar()));
        data.setPhoneNumber(userData.getPhoneNumber());
        data.setDisplayName(userData.getFullname());
        data.setReferralCode(userData.getReferrals().getCode());
        return data;
    }

    @Override
    @Cacheable(value = "getProfileData",key = "#email")
    public ProfileResponseDto getProfileData(String email) {
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        ProfileResponseDto data = new ProfileResponseDto();
        data.setEmail(email);
        data.setDisplayName(userData.getFullname());
        data.setAvatar(cloudinaryService.generateUrl(userData.getAvatar()));
        data.setPhoneNumber(userData.getPhoneNumber());
        data.setDisplayName(userData.getFullname());
        data.setReferralCode(userData.getReferrals().getCode());
        return data;
    }

    @Override
    public ChangePasswordResponseDto changePassword(ChangePasswordRequestDto data, String email) {
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

    @Override
    public Users getUserByEmail(String email) {
        log.info("Requesting User Detail: " + email);
        return userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
    }
}
