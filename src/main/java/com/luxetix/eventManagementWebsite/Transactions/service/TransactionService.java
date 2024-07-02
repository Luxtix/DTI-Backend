package com.luxetix.eventManagementWebsite.Transactions.service;

import com.luxetix.eventManagementWebsite.Transactions.dto.TransactionRequestDto;
import com.luxetix.eventManagementWebsite.Transactions.entity.Transactions;

import java.util.List;

public interface TransactionService {
    Transactions newTransaction(TransactionRequestDto data, String email);
    List<Transactions> getAllTransactions(long userId);
}
