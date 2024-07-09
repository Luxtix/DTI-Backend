package com.luxtix.eventManagementWebsite.vouchers.repository;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Vouchers,Long> {
    public static final String getRemainingVoucherCount = "SELECT\n" +
            "    (v.voucherLimit - COALESCE(COUNT(tr.id), 0))" +
            "FROM\n" +
            "    Vouchers v\n" +
            "LEFT JOIN\n" +
            "    Transactions tr ON v.id = tr.vouchers.id\n" +
            "WHERE\n" +
            "    v.id = :voucherId GROUP BY v.voucherLimit\n";

    List<Vouchers> findByEventsId (long eventId);



    @Query(getRemainingVoucherCount)
    int getRemainingTicket(@Param("voucherId") long voucherId);

}
