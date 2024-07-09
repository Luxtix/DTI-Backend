package com.luxtix.eventManagementWebsite.transactionList.service;

import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionDetailResponseDto;
import com.luxtix.eventManagementWebsite.transactionList.entity.TransactionList;

import java.util.List;

public interface TransactionListService {
    void AddNewTransactionList(TransactionList transactionList);

    List<TransactionList> getAllTransactionDetail(long transactionId);
}
