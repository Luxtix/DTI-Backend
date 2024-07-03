package com.luxetix.eventManagementWebsite.pointHistory.service.impl;


import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.pointHistory.dao.PointHistoryDao;
import com.luxetix.eventManagementWebsite.pointHistory.dto.PointHistoryResponseDto;
import com.luxetix.eventManagementWebsite.pointHistory.entity.PointHistory;
import com.luxetix.eventManagementWebsite.pointHistory.repository.PointHistoryRepository;
import com.luxetix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import com.luxetix.eventManagementWebsite.users.service.UserService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class PointHistoryServiceImpl implements PointHistoryService {

    private final UserService userService;
    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryServiceImpl(UserService userService, PointHistoryRepository pointHistoryRepository) {
        this.userService = userService;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Override
    public PointHistoryResponseDto getUserPoint(String email) {
        Instant expiredDate = Instant.now().minus(90, ChronoUnit.DAYS);
        Users userData = userService.getUserByEmail(email);
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
    public void addNewPointHistory(PointHistory pointHistory) {
        pointHistoryRepository.save(pointHistory);
    }
}
