package com.luxtix.eventManagementWebsite.tickets.dao;

import java.time.Instant;
import java.time.LocalDate;

public interface TicketSummaryDao {
    Instant getDate();
    int getTotalQty();
}
