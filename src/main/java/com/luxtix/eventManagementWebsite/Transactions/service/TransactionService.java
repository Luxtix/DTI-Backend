package com.luxtix.eventManagementWebsite.Transactions.service;

import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionDetailResponseDto;
import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionListResponseDto;
import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionRequestDto;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.transactionList.entity.TransactionList;

import java.util.List;

public interface TransactionService {
    Transactions newTransaction(TransactionRequestDto data, String email);
    List<TransactionDetailResponseDto> getAllTransactionDetail(long transactionId);


    List<TransactionListResponseDto> getAllTransactions(long userId);
}
