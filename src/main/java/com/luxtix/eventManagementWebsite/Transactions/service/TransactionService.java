package com.luxtix.eventManagementWebsite.Transactions.service;

import com.luxtix.eventManagementWebsite.Transactions.dto.*;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.transactionList.entity.TransactionList;
import org.springframework.data.domain.Page;
import java.util.List;

public interface TransactionService {
    Transactions newTransaction(TransactionRequestDto data, String email);
    List<TransactionDetailResponseDto> getAllTransactionDetail(long transactionId);


    Page<TransactionListResponseDto> getAllTransactions(long userId, int page, int page_size);


    int getEventTotalRevenue(long eventId, String dateType);


    CalculatePriceResponseDto getCalculateTransaction(CalculatePriceRequestDto calculatePriceRequestDto);

}
