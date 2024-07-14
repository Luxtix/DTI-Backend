package com.luxtix.eventManagementWebsite.events.transaction;

import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionListResponseDto;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.Transactions.repository.TransactionRepository;
import com.luxtix.eventManagementWebsite.Transactions.service.impl.TransactionServiceImpl;
import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxtix.eventManagementWebsite.transactionList.service.TransactionListService;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class GetAllTransactionTest {

    @Mock
    private UserService userService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionListService transactionListService;
    @Mock
    private UserUsageReferralsService userUsageReferralsService;
    @Mock
    private VoucherService voucherService;
    @Mock
    private CloudinaryService cloudinaryService;
    @Mock
    private PointHistoryService pointHistoryService;

    @Mock
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(userService, transactionRepository, transactionListService, userUsageReferralsService, voucherService, cloudinaryService, pointHistoryService);
    }



    @Test
    public void test_returns_list_of_transactions_for_valid_userId() {
        long userId = 1L;
        List<Transactions> transactions = new ArrayList<>();
        Transactions transaction = new Transactions();
        Events event = new Events();
        event.setId(1L);
        event.setName("Event Name");
        event.setEventDate(LocalDate.now().plusDays(1));
        event.setEventImage("image.jpg");
        transaction.setEvents(event);
        transactions.add(transaction);
        Pageable pageable = PageRequest.of(0,1);
        Page<Transactions> transactionPage = new PageImpl<>(transactions, pageable, transactions.size());
        Mockito.when(transactionRepository.findByUsersId(userId, pageable)).thenReturn(Optional.of(transactionPage));
        Mockito.when(cloudinaryService.generateUrl("image.jpg")).thenReturn("http://image.url");

        Page<TransactionListResponseDto> result = transactionService.getAllTransactions(userId,0,1);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Event Name", result.getContent().get(0).getEventName());
    }



    @Test
    public void test_throws_DataNotFoundException_when_no_transactions_found_for_userId() {
        long userId = 1L;
        Pageable pageable = PageRequest.of(0,1);
        Mockito.when(transactionRepository.findByUsersId(userId,pageable)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            transactionService.getAllTransactions(userId,0,1);
        });
    }
}
