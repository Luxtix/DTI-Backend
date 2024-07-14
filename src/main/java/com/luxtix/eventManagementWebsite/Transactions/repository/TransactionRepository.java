package com.luxtix.eventManagementWebsite.Transactions.repository;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long> {
    Optional<Page<Transactions>> findByUsersId(long userId, Pageable pageable);

    public static final String eventRevenueDataQuery = "SELECT COALESCE(SUM(tr.finalPrice), 0) FROM Transactions tr WHERE tr.events.id = :eventId AND DATE_TRUNC(:dateFilter, tr.createdAt) = DATE_TRUNC(:dateFilter, CURRENT_TIMESTAMP)";

    @Query(value = eventRevenueDataQuery)
    int getTotalEventRevenue(long eventId, String dateFilter);
}
