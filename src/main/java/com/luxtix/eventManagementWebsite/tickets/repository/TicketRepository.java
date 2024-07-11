package com.luxtix.eventManagementWebsite.tickets.repository;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TicketRepository extends JpaRepository<Tickets,Long> {


    public static final String getRemainingTicketCount = "SELECT t.qty - COALESCE((SELECT SUM(tl.qty) FROM TransactionList tl WHERE tl.tickets.id = t.id),0) as remainingQty from Tickets t where t.id = :ticketId";


    public static final String lowestTicketPriceQuery = "SELECT COALESCE(MIN(t.price), 0) from Tickets  t WHERE t.events.id = :eventId";


    public static final String ticketSoldQuantityQuery = "SELECT COALESCE(SUM(tr.totalQty), 0) FROM Transactions tr WHERE tr.events.id = :eventId AND DATE_TRUNC(:dateFilter, tr.createdAt) = DATE_TRUNC(:dateFilter, CURRENT_TIMESTAMP)";


    public static final String totalTicketInEventQuery = "SELECT COALESCE(SUM(t.qty), 0) from Tickets t where t.events.id = :eventId";

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


    List<Tickets> findByEventsId(long eventId);



    @Query(value = getRemainingTicketCount)
    int getRemainingTicket(@Param("ticketId") long ticketId);

  
    @Query(value = ticketSoldQuantityQuery)
    int getTicketSoldQuantity(@Param("eventId") long eventId,@Param("dateFilter") String dateFilter) ;



    @Query(value = lowestTicketPriceQuery)
    int getLowestTicketPrice(@Param("eventId") long eventId);


    @Query(value = totalTicketInEventQuery)
    int getTotalTicketInEvent(@Param("eventId") long eventId);

}
