//package com.luxtix.eventManagementWebsite.events.transaction;
//

//import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionRequestDto;
//import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
//import com.luxtix.eventManagementWebsite.Transactions.repository.TransactionRepository;
//import com.luxtix.eventManagementWebsite.Transactions.service.TransactionService;
//import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
//import com.luxtix.eventManagementWebsite.pointHistory.service.PointHistoryService;
//import com.luxtix.eventManagementWebsite.transactionList.service.TransactionListService;
//import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
//import com.luxtix.eventManagementWebsite.users.entity.Users;
//import com.luxtix.eventManagementWebsite.users.service.impl.UserServiceImpl;
//import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.List;

//
//
//@ExtendWith(MockitoExtension.class)
//public class AddNewTransactionTest {
//
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//
//    @InjectMocks
//    private PointHistoryService pointHistoryService;
//
//
//    @InjectMocks
//    private TransactionListService transactionListService;
//
//    @InjectMocks
//    private UserUsageReferralsService userUsageReferralsService;
//
//
//    @InjectMocks
//    private CloudinaryService cloudinaryService;
//
//
//    @InjectMocks
//    private TransactionService transactionService;
//
//
//    @InjectMocks
//    private TransactionRepository transactionRepository;
//
//
//    @InjectMocks
//    private VoucherService voucherService;
//
//
//
//
//
//    @Test
//    public void test_successfully_creates_new_transaction_with_valid_data_and_email() {
//        String email = "test@example.com";
//        Users user = new Users();
//        user.setEmail(email);
//        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
//
//        TransactionRequestDto data = new TransactionRequestDto();
//        data.setEventId(1L);
//        data.setVoucherId(1L);

//        data.setTotalPrice(100);
//        data.setTotalQty(2);
//        data.setUsePoint(10);
//        List<TransactionRequestDto.TransactionTicketDto> tickets = new ArrayList<>();
//        TransactionRequestDto.TransactionTicketDto ticketDto = new TransactionRequestDto.TransactionTicketDto();
//        ticketDto.setTicketId(1L);
//        ticketDto.setPrice(50);
//        ticketDto.setQty(2);
//        tickets.add(ticketDto);
//        data.setTickets(tickets);
//

//        Transactions result = transactionService.newTransaction(data, email);
//
//        assertNotNull(result);
//        assertEquals(data.getVoucherId(),result.getVouchers().getId());


//        assertEquals(user, result.getUsers());
//        assertEquals(data.getTotalPrice(), result.getTotalPrice());
//        assertEquals(data.getTotalQty(), result.getTotalQty());
//    }
//}
