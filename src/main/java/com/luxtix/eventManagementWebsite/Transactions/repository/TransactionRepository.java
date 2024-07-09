package com.luxtix.eventManagementWebsite.Transactions.repository;
import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionDetailResponseDto;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long> {
    Optional<List<Transactions>> findByUsersId(long userId);

}
