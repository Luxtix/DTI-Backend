package com.luxtix.eventManagementWebsite.pointHistory.service;

import com.luxtix.eventManagementWebsite.pointHistory.dto.PointHistoryResponseDto;
import com.luxtix.eventManagementWebsite.pointHistory.entity.PointHistory;

public interface PointHistoryService {
    PointHistoryResponseDto getUserPoint(String email);

    void addNewPointHistory(PointHistory pointHistory);
}
