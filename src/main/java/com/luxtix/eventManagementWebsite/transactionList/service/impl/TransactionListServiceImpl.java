package com.luxtix.eventManagementWebsite.transactionList.service.impl;

import com.luxtix.eventManagementWebsite.transactionList.entity.TransactionList;
import com.luxtix.eventManagementWebsite.transactionList.repository.TransactionListRepository;
import com.luxtix.eventManagementWebsite.transactionList.service.TransactionListService;
import org.springframework.stereotype.Service;


@Service
public class TransactionListServiceImpl implements TransactionListService {

    private final TransactionListRepository transactionListRepository;

    public TransactionListServiceImpl(TransactionListRepository transactionListRepository) {
        this.transactionListRepository = transactionListRepository;
    }

    @Override
    public void AddNewTransactionList(TransactionList transactionList) {
        transactionListRepository.save(transactionList);
    }
}
