package com.luxtix.eventManagementWebsite.tickets.repository;

import com.luxtix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TicketRepository extends JpaRepository<Tickets,Long> {
    public static final String eventTicketQuery = "SELECT t.id as ticketId, t.name as ticketName, t.price as ticketPrice, t.qty as ticketQuantity, t.qty - COALESCE(SUM(tl.qty),0) as remainingQty from Tickets t left join Events e on t.events.id = e.id left join TransactionList tl on t.id = tl.tickets.id where e.id = :eventId GROUP BY t.id";
    @Query(value = "SELECT ds.date_start AS date, COALESCE(SUM(tr.total_qty), 0) AS total_qty " +
            "FROM (SELECT DISTINCT DATE_TRUNC(CAST(:intervalStart AS text), generate_series(CAST(:startDate AS timestamp), CAST(:endDate AS timestamp), CAST(:intervalTime AS interval))) AS date_start) ds " +
            "LEFT JOIN transactions tr ON DATE_TRUNC(CAST(:intervalTo AS text), tr.created_at) = ds.date_start " +
            "LEFT JOIN events e ON e.id = tr.event_id " +
            "WHERE (e.id = :eventId OR e.id IS NULL) " +
            "GROUP BY ds.date_start " +
            "ORDER BY ds.date_start",
            nativeQuery = true)

    List<TicketSummaryDao> getTransactionTicketSummary(    @Param("intervalStart") String intervalStart,
                                                           @Param("startDate") String startDate,
                                                           @Param("endDate") String endDate,
                                                           @Param("intervalTime") String intervalTime,
                                                           @Param("intervalTo") String intervalTo,
                                                           @Param("eventId") long eventId);

    @Query(value = eventTicketQuery)
    List<TicketDao> getEventTicket(@Param("eventId") Long eventId);
}
