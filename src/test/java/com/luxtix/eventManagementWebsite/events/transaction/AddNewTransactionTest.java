//package com.luxtix.eventManagementWebsite.events.transaction;
//
//import com.luxtix.eventManagementWebsite.users.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//@ExtendWith(MockitoExtension.class)
//public class AddNewTransactionTest {
//
//
//    @InjectMocks
//    private UserService userService;
//
//    public AddNewTransactionTest(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Test
//    public void test_successfully_creates_new_transaction_with_valid_data_and_email() {
//        // Arrange
//        UserService userService = Mock.(UserService.class);
//        TransactionRepository transactionRepository = mock(TransactionRepository.class);
//        TransactionListService transactionListService = mock(TransactionListService.class);
//        UserUsageReferralsService userUsageReferralsService = mock(UserUsageReferralsService.class);
//        VoucherService voucherService = mock(VoucherService.class);
//        CloudinaryService cloudinaryService = mock(CloudinaryService.class);
//        PointHistoryService pointHistoryService = mock(PointHistoryService.class);
//
//        TransactionServiceImpl transactionService = new TransactionServiceImpl(
//                userService, transactionRepository, transactionListService, userUsageReferralsService,
//                voucherService, cloudinaryService, pointHistoryService
//        );
//
//        String email = "test@example.com";
//        Users user = new Users();
//        user.setEmail(email);
//        when(userService.getUserByEmail(email)).thenReturn(user);
//
//        TransactionRequestDto data = new TransactionRequestDto();
//        data.setEventId(1L);
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
//        // Act
//        Transactions result = transactionService.newTransaction(data, email);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(user, result.getUsers());
//        assertEquals(data.getTotalPrice(), result.getTotalPrice());
//        assertEquals(data.getTotalQty(), result.getTotalQty());
//    }
//}
