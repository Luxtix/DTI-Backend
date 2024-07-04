package com.luxtix.eventManagementWebsite.transactionList.repository;

import com.luxtix.eventManagementWebsite.transactionList.entity.TransactionList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionListRepository extends JpaRepository<TransactionList,Long> {
}
