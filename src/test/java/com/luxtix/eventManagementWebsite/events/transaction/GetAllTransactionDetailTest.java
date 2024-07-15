package com.luxtix.eventManagementWebsite.events.transaction;


import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionDetailResponseDto;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.Transactions.repository.TransactionRepository;
import com.luxtix.eventManagementWebsite.Transactions.service.impl.TransactionServiceImpl;
import com.luxtix.eventManagementWebsite.city.entity.Cities;
import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxtix.eventManagementWebsite.transactionList.entity.TransactionList;
import com.luxtix.eventManagementWebsite.transactionList.service.TransactionListService;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GetAllTransactionDetailTest {

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
    public void test_returns_list_of_transaction_details_when_valid_transaction_id_provided() {

        List<TransactionList> transactionLists = new ArrayList<>();
        TransactionList transactionList = new TransactionList();
        Transactions transactions = new Transactions();
        Events events = new Events();
        Cities cities = new Cities();
        Tickets tickets = new Tickets();

        cities.setName("CityName");
        events.setName("EventName");
        events.setCities(cities);
        events.setEventImage("eventImage");
        events.setVenueName("Venue");
        events.setEventDate(LocalDate.now());
        events.setStartTime(LocalTime.now());
        events.setEndTime(LocalTime.now());
        events.setIsOnline(true);
        transactions.setEvents(events);
        tickets.setName("TicketName");
        tickets.setQty(2);
        transactionList.setTransactions(transactions);
        transactionList.setTickets(tickets);
        transactionLists.add(transactionList);

        Mockito.when(transactionListService.getAllTransactionDetail(ArgumentMatchers.anyLong())).thenReturn(transactionLists);
        Mockito.when(cloudinaryService.generateUrl(ArgumentMatchers.anyString())).thenReturn("generatedUrl");

        TransactionDetailResponseDto result = transactionService.getAllTransactionDetail(1L);

        assertNotNull(result);
        assertEquals("EventName", result.getEventName());
    }



    @Test
    public void test_returns_empty_list_when_no_transaction_details_found_for_given_transaction_id() {
        Mockito.when(transactionListService.getAllTransactionDetail(ArgumentMatchers.anyLong())).thenReturn(new ArrayList<>());

       TransactionDetailResponseDto result = transactionService.getAllTransactionDetail(1L);

        assertNotNull(result);
    }
}
