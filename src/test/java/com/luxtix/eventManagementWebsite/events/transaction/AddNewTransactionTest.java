<<<<<<< HEAD
package com.luxtix.eventManagementWebsite.events.transaction;

import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionRequestDto;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.Transactions.repository.TransactionRepository;
import com.luxtix.eventManagementWebsite.Transactions.service.impl.TransactionServiceImpl;
import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryServiceImpl;
import com.luxtix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxtix.eventManagementWebsite.pointHistory.service.impl.PointHistoryServiceImpl;
import com.luxtix.eventManagementWebsite.referrals.entity.Referrals;
import com.luxtix.eventManagementWebsite.transactionList.service.TransactionListService;
import com.luxtix.eventManagementWebsite.transactionList.service.impl.TransactionListServiceImpl;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.impl.UserUsageReferralsServiceImpl;
import com.luxtix.eventManagementWebsite.users.entity.RolesType;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.repository.UserRepository;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import com.luxtix.eventManagementWebsite.users.service.impl.UserServiceImpl;
import com.luxtix.eventManagementWebsite.vouchers.dto.VoucherDto;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import com.luxtix.eventManagementWebsite.vouchers.service.impl.VoucherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class AddNewTransactionTest {

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
    public void test_successfully_creates_new_transaction_with_valid_data_and_email_with_voucher_id_not_null() {
        String email = "test@example.com";
        Users user = new Users();
        user.setId(1L);
        user.setEmail(email);
        user.setFullname("mamamma");
        user.setPassword("hajakak");
        Referrals referrals = new Referrals();
        referrals.setCode("blablabla");
        user.setReferrals(referrals);
        user.setRole(RolesType.USER);

        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);

        TransactionRequestDto data = new TransactionRequestDto();
        data.setEventId(1L);
        data.setTotalQty(2);
        data.setTotalPrice(100);
        data.setUsePoint(10);
        List<TransactionRequestDto.TransactionTicketDto> tickets = new ArrayList<>();
        TransactionRequestDto.TransactionTicketDto ticketDto = new TransactionRequestDto.TransactionTicketDto();
        ticketDto.setTicketId(1L);
        ticketDto.setPrice(50);
        ticketDto.setQty(2);
        tickets.add(ticketDto);
        data.setVoucherId(1L);
        data.setTickets(tickets);
        Vouchers vouchers = new Vouchers();
        vouchers.setId(1L);
        vouchers.setReferralOnly(false);
        TransactionServiceImpl transactionService = new TransactionServiceImpl(userService, transactionRepository, transactionListService, userUsageReferralsService, voucherService, cloudinaryService, pointHistoryService);
        Mockito.when(voucherService.getVoucherById(1L)).thenReturn(vouchers);
        Transactions result = transactionService.newTransaction(data, email);
        assertNotNull(result);
        assertEquals(user, result.getUsers());
        assertEquals(data.getTotalPrice(), result.getTotalPrice());
        assertEquals(data.getTotalQty(), result.getTotalQty());
        assertEquals(data.getVoucherId(),result.getVouchers().getId());
    }


    @Test
    public void test_successfully_creates_new_transaction_with_valid_data_and_email_with_voucher_id_is_null() {
        String email = "test@example.com";
        Users user = new Users();
        user.setId(1L);
        user.setEmail(email);
        user.setFullname("mamamma");
        user.setPassword("hajakak");
        Referrals referrals = new Referrals();
        referrals.setCode("blablabla");
        user.setReferrals(referrals);
        user.setRole(RolesType.USER);

        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);

        TransactionRequestDto data = new TransactionRequestDto();
        data.setEventId(1L);
        data.setTotalQty(2);
        data.setTotalPrice(100);
        data.setUsePoint(10);
        List<TransactionRequestDto.TransactionTicketDto> tickets = new ArrayList<>();
        TransactionRequestDto.TransactionTicketDto ticketDto = new TransactionRequestDto.TransactionTicketDto();
        ticketDto.setTicketId(1L);
        ticketDto.setPrice(50);
        ticketDto.setQty(2);
        tickets.add(ticketDto);
        data.setVoucherId(null);
        data.setTickets(tickets);

        TransactionServiceImpl transactionService = new TransactionServiceImpl(userService, transactionRepository, transactionListService, userUsageReferralsService, voucherService, cloudinaryService, pointHistoryService);

        Transactions result = transactionService.newTransaction(data, email);
        assertNotNull(result);
        assertEquals(user, result.getUsers());
        assertEquals(data.getTotalPrice(), result.getTotalPrice());
        assertEquals(data.getTotalQty(), result.getTotalQty());
        assertEquals(null,result.getVouchers());
    }

}
=======
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
>>>>>>> ee70bc7 (Feat(Events):Add public endpoint for accessing the public event list and event details (#21))
