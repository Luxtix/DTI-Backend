package com.luxetix.eventManagementWebsite.pointHistory.repository;

import com.luxetix.eventManagementWebsite.pointHistory.PointHistory;
import com.luxetix.eventManagementWebsite.refferals.entity.Referrals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory,Long> {
}
