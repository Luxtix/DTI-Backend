package com.luxtix.eventManagementWebsite.Transactions.repository;

import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long> {
    List<Transactions> findByUsersId(long userId);
}
