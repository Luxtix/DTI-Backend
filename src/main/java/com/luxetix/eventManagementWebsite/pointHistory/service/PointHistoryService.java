package com.luxetix.eventManagementWebsite.pointHistory.service;

import com.luxetix.eventManagementWebsite.pointHistory.dto.PointHistoryResponseDto;
import com.luxetix.eventManagementWebsite.pointHistory.entity.PointHistory;

public interface PointHistoryService {
    PointHistoryResponseDto getUserPoint();

    void addPointHistory(PointHistory pointHistory);
}
