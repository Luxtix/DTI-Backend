package com.luxetix.eventManagementWebsite.transactionList.service.impl;

import com.luxetix.eventManagementWebsite.transactionList.entity.TransactionList;
import com.luxetix.eventManagementWebsite.transactionList.repository.TransactionListRepository;
import com.luxetix.eventManagementWebsite.transactionList.service.TransactionListService;
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
