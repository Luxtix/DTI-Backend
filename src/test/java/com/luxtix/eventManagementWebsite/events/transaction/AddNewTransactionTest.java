

package com.luxtix.eventManagementWebsite.events.transaction;
import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionRequestDto;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.Transactions.repository.TransactionRepository;
import com.luxtix.eventManagementWebsite.Transactions.service.impl.TransactionServiceImpl;
import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.eventReviews.service.EventReviewService;
import com.luxtix.eventManagementWebsite.pointHistory.entity.PointHistory;
import com.luxtix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxtix.eventManagementWebsite.referrals.entity.Referrals;
import com.luxtix.eventManagementWebsite.transactionList.service.TransactionListService;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import com.luxtix.eventManagementWebsite.users.entity.RolesType;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;




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

    @Mock
    private EventReviewService eventReviewService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(userService,transactionRepository, transactionListService, userUsageReferralsService, voucherService,  eventReviewService,  cloudinaryService, pointHistoryService);
    }

    @Test
    public void test_successfully_creates_new_transaction_with_valid_data_and_email_with_voucher_id_not_null_and_using_point() {
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
        data.setOriginalPrice(BigDecimal.valueOf(200000));
        data.setTotalDiscount(BigDecimal.valueOf(10000));
        data.setFinalPrice(BigDecimal.valueOf(190000));
        data.setUsePoint(1000);
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


        PointHistory pointHistory = new PointHistory();
        pointHistory.setId(1L);
        pointHistory.setUsers(user);
        pointHistory.setTotalPoint(data.getUsePoint());
        pointHistoryService.addNewPointHistory(pointHistory);
        Mockito.verify(pointHistoryService).addNewPointHistory(pointHistory);
        Mockito.when(voucherService.getVoucherById(1L)).thenReturn(vouchers);
        Transactions result = transactionService.newTransaction(data, email);
        assertNotNull(result);
        assertEquals(user, result.getUsers());

        assertEquals(data.getFinalPrice(), result.getFinalPrice());

        assertEquals(data.getTotalQty(), result.getTotalQty());
        assertEquals(data.getVoucherId(), result.getVouchers().getId());
    }


    @Test
    public void test_successfully_creates_new_transaction_with_valid_data_and_email_with_voucher_id_is_null_and_not_using_point() {
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
        data.setOriginalPrice(BigDecimal.valueOf(200000));
        data.setTotalDiscount(BigDecimal.valueOf(0));
        data.setFinalPrice(BigDecimal.valueOf(200000));
        data.setUsePoint(null);
        List<TransactionRequestDto.TransactionTicketDto> tickets = new ArrayList<>();
        TransactionRequestDto.TransactionTicketDto ticketDto = new TransactionRequestDto.TransactionTicketDto();
        ticketDto.setTicketId(1L);
        ticketDto.setPrice(50);
        ticketDto.setQty(2);
        tickets.add(ticketDto);
        data.setVoucherId(null);
        data.setTickets(tickets);
        Transactions result = transactionService.newTransaction(data, email);
        assertNotNull(result);
        assertEquals(user, result.getUsers());
        assertEquals(data.getFinalPrice(), result.getFinalPrice());
        assertEquals(data.getTotalQty(), result.getTotalQty());
        assertNull(result.getVouchers());
    }
}
