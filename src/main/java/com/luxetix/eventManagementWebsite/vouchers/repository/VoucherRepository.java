package com.luxetix.eventManagementWebsite.vouchers.repository;

import com.luxetix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxetix.eventManagementWebsite.vouchers.dao.VoucherDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Vouchers,Long> {
    public static final String eventVoucherQuery = "select v.id as voucherId, v.name as voucherName, v.rate as voucherRate , v.startDate as startDate , v.endDate as endDate, v.voucherLimit as voucherLimit, v.voucherLimit - COUNT(DISTINCT tr.id) as remainingVoucherLimit, v.referralOnly as referralOnly from Vouchers v left join Events e on v.events.id = e.id left join Transactions tr on v.id = tr.vouchers.id WHERE e.id = :eventId\n" +
            "  AND (\n" +
            "    (:isReferral = TRUE AND (v.startDate IS NULL OR v.startDate <= CURRENT_DATE) AND (v.endDate IS NULL OR v.endDate >= CURRENT_DATE))\n" +
            "    OR (v.referralOnly = FALSE AND (v.startDate IS NULL OR (v.startDate IS NOT NULL AND v.endDate IS NOT NULL AND CURRENT_DATE BETWEEN v.startDate AND v.endDate)))\n" +
            "  ) GROUP BY v.id";
    @Query(value = eventVoucherQuery)
    List<VoucherDao> getEventVoucher(@Param("eventId") Long eventId, @Param("isReferral") boolean isReferral);
}
