package com.luxetix.eventManagementWebsite.Transactions.repository;

import com.luxetix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long> {
    List<Transactions> findByUsersId(long userId);
}
