package com.luxetix.eventManagementWebsite.pointHistory.service.impl;


import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.pointHistory.dao.PointHistoryDao;
import com.luxetix.eventManagementWebsite.pointHistory.dto.PointHistoryResponseDto;
import com.luxetix.eventManagementWebsite.pointHistory.entity.PointHistory;
import com.luxetix.eventManagementWebsite.pointHistory.repository.PointHistoryRepository;
import com.luxetix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class PointHistoryServiceImpl implements PointHistoryService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryServiceImpl(UserRepository userRepository, PointHistoryRepository pointHistoryRepository) {
        this.userRepository = userRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Override
    public PointHistoryResponseDto getUserPoint() {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        Instant expiredDate = Instant.now().minus(90, ChronoUnit.DAYS);
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("You are not logged in yet"));
        PointHistoryDao data = pointHistoryRepository.getUserPoint(userData.getId(),expiredDate).orElse(null);
        PointHistoryResponseDto points = new PointHistoryResponseDto();
        if(data == null){
            points.setPoints(0);
        }else{
            points.setPoints(data.getPoints());
        }

        return points;
    }

    @Override
    public void addPointHistory(PointHistory pointHistory) {
        pointHistoryRepository.save(pointHistory);
    }
}
