package com.luxtix.eventManagementWebsite.dashboard.service;

import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventSummaryResponseDto;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;

import java.util.List;

public interface DashboardService {
    DashboardEventSummaryResponseDto getSummaryData(long eventId, String dateType);

}
