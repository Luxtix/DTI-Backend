package com.luxtix.eventManagementWebsite.Transactions.service;

import com.luxtix.eventManagementWebsite.Transactions.dto.GetTransactionResponseDto;
import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionRequestDto;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;

import java.util.List;

public interface TransactionService {
    Transactions newTransaction(TransactionRequestDto data, String email);
    List<GetTransactionResponseDto> getAllTransactions(long userId);
}
