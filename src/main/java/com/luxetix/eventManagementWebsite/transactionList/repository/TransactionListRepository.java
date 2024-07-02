package com.luxetix.eventManagementWebsite.transactionList.repository;

import com.luxetix.eventManagementWebsite.transactionList.entity.TransactionList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionListRepository extends JpaRepository<TransactionList,Long> {
}
