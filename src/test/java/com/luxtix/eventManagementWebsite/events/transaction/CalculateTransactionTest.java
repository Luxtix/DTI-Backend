package com.luxtix.eventManagementWebsite.events.transaction;

import com.luxtix.eventManagementWebsite.Transactions.dto.CalculatePriceRequestDto;
import com.luxtix.eventManagementWebsite.Transactions.dto.CalculatePriceResponseDto;
import com.luxtix.eventManagementWebsite.Transactions.repository.TransactionRepository;
import com.luxtix.eventManagementWebsite.Transactions.service.impl.TransactionServiceImpl;
import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.eventReviews.service.EventReviewService;
import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxtix.eventManagementWebsite.transactionList.service.TransactionListService;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class CalculateTransactionTest {

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
    public void test_handle_null_voucher_id() {
        CalculatePriceRequestDto requestDto = new CalculatePriceRequestDto();
        requestDto.setVoucherId(null);
        requestDto.setOriginalPrice(new BigDecimal("100.00"));
        requestDto.setUsePoint(10);

        // Call the method under test
        CalculatePriceResponseDto responseDto = transactionService.getCalculateTransaction(requestDto);

        assertEquals(new BigDecimal("90.00"), responseDto.getFinalPrice());
        assertEquals(new BigDecimal("10.00"), responseDto.getTotalDiscount());
        assertEquals(new BigDecimal("100.00"), responseDto.getOriginalPrice());
    }



    @Test
    public void test_correctly_calculates_final_price_and_total_discount_with_voucher() {

        CalculatePriceRequestDto requestDto = new CalculatePriceRequestDto();
        requestDto.setVoucherId(1L);
        requestDto.setOriginalPrice(new BigDecimal("100.00"));
        requestDto.setUsePoint(10);

        Vouchers voucher = new Vouchers();
        voucher.setRate(new BigDecimal("10"));
        Mockito.when(voucherService.getVoucherById(1L)).thenReturn(voucher);

        CalculatePriceResponseDto responseDto = transactionService.getCalculateTransaction(requestDto);

        assertEquals(new BigDecimal("80.00"), responseDto.getFinalPrice());
        assertEquals(new BigDecimal("20.00"), responseDto.getTotalDiscount());
        assertEquals(new BigDecimal("100.00"), responseDto.getOriginalPrice());
    }

}
