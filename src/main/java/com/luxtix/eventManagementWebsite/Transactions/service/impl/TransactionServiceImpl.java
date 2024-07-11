package com.luxtix.eventManagementWebsite.Transactions.service.impl;

import com.luxtix.eventManagementWebsite.Transactions.dto.*;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.Transactions.repository.TransactionRepository;
import com.luxtix.eventManagementWebsite.Transactions.service.TransactionService;
import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.pointHistory.entity.PointHistory;
import com.luxtix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxtix.eventManagementWebsite.transactionList.entity.TransactionList;
import com.luxtix.eventManagementWebsite.transactionList.service.TransactionListService;
import com.luxtix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Service
public class TransactionServiceImpl implements TransactionService {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final TransactionListService transactionListService;
    private final UserUsageReferralsService userUsageReferralsService;
    private final VoucherService voucherService;
    private final CloudinaryService cloudinaryService;



    private final PointHistoryService pointHistoryService;
    public TransactionServiceImpl(UserService userService, TransactionRepository transactionRepository, TransactionListService transactionListService, UserUsageReferralsService userUsageReferralsService, VoucherService voucherService, CloudinaryService cloudinaryService, PointHistoryService pointHistoryService) {
        this.userService = userService;
        this.transactionRepository = transactionRepository;
        this.transactionListService = transactionListService;
        this.userUsageReferralsService = userUsageReferralsService;
        this.voucherService = voucherService;
        this.cloudinaryService = cloudinaryService;
        this.pointHistoryService = pointHistoryService;
    }

    @Override
    @Transactional
    public Transactions newTransaction(TransactionRequestDto data,String email) {
        Transactions newTransactions = new Transactions();
        Users userData = userService.getUserByEmail(email);
        newTransactions.setUsers(userData);
        Events event = new Events();
        event.setId(data.getEventId());
        newTransactions.setEvents(event);
        newTransactions.setFinalPrice(data.getFinalPrice());
        newTransactions.setTotalDiscount(data.getTotalDiscount());
        newTransactions.setOriginalPrice(data.getOriginalPrice());
        newTransactions.setTotalQty(data.getTotalQty());
        Vouchers voucher = new Vouchers();
        if(data.getVoucherId() != null){
            voucher.setId(data.getVoucherId());
            Vouchers voucherData = voucherService.getVoucherById(data.getVoucherId());
            if(voucherData.getReferralOnly()){
                UserUsageReferrals referralUsageData = userUsageReferralsService.getUserUsageReferralData(userData.getId());
                referralUsageData.setBenefitClaim(true);
                userUsageReferralsService.addNewUserUsageReferralData(referralUsageData);
            }
            newTransactions.setVouchers(voucher);
        }
        transactionRepository.save(newTransactions);
        if(data.getUsePoint() != null){
            PointHistory newPoint = new PointHistory();
            newPoint.setUsers(userData);
            newPoint.setTotalPoint(Math.negateExact(data.getUsePoint()));
            pointHistoryService.addNewPointHistory(newPoint);
        }
        for(TransactionRequestDto.TransactionTicketDto ticketData: data.getTickets()){
            TransactionList transactionTicketData = new TransactionList();
            Tickets ticket = new Tickets();
            ticket.setId(ticketData.getTicketId());
            transactionTicketData.setTickets(ticket);
            transactionTicketData.setQty(ticketData.getQty());
            transactionTicketData.setPrice(ticketData.getPrice());
            transactionTicketData.setTransactions(newTransactions);
            transactionListService.AddNewTransactionList(transactionTicketData);
        }
        return newTransactions;
    }


    @Override
    public List<TransactionDetailResponseDto> getAllTransactionDetail(long transactionId){
        List<TransactionList> transactionLists =transactionListService.getAllTransactionDetail(transactionId);
        List<TransactionDetailResponseDto> list = new ArrayList<>();
        for(TransactionList transaction : transactionLists){
            TransactionDetailResponseDto data = new TransactionDetailResponseDto();
            data.setEventName(transaction.getTransactions().getEvents().getName());
            data.setCityName(transaction.getTransactions().getEvents().getCities().getName());
            data.setEventImage(cloudinaryService.generateUrl(transaction.getTransactions().getEvents().getEventImage()));
            data.setVenue(transaction.getTransactions().getEvents().getVenueName());
            data.setEventDate(transaction.getTransactions().getEvents().getEventDate());
            data.setId(transaction.getId());
            data.setIsOnline(transaction.getTransactions().getEvents().getIsOnline());
            data.setTicketName(transaction.getTickets().getName());
            data.setTicketQty(transaction.getTickets().getQty());
            data.setStartTime(transaction.getTransactions().getEvents().getStartTime());
            data.setEndTime(transaction.getTransactions().getEvents().getEndTime());
            DayOfWeek day = transaction.getTransactions().getEvents().getEventDate().getDayOfWeek();
            String eventDay = day.getDisplayName(
                    java.time.format.TextStyle.FULL,
                    Locale.ENGLISH
            );
            data.setEventDay(eventDay);
            list.add(data);
        }
        return list;
    }

    @Override
    public Page<TransactionListResponseDto> getAllTransactions(long userId,int page, int page_size) {
        Pageable pageable = PageRequest.of(page, page_size);
        Page<Transactions> transactions = transactionRepository.findByUsersId(userId,pageable).orElseThrow(() -> new DataNotFoundException("Transaction is empty"));
        List<TransactionListResponseDto> transactionList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for(Transactions transactionData : transactions){
            TransactionListResponseDto newTransaction = new TransactionListResponseDto();
            newTransaction.setEventId(transactionData.getEvents().getId());
            newTransaction.setEventName(transactionData.getEvents().getName());
            newTransaction.setEventDate(transactionData.getEvents().getEventDate());
            newTransaction.setTransactionId(transactionData.getId());
            newTransaction.setEventImage(cloudinaryService.generateUrl(transactionData.getEvents().getEventImage()));
            if(!transactionData.getEvents().getEventDate().isAfter(currentDate)){
                newTransaction.setIsDone(true);
            }else{
                newTransaction.setIsDone(false);
            }
            transactionList.add(newTransaction);
        }
        return  new PageImpl<>(transactionList,pageable,transactions.getTotalElements());
    }



    @Override
    public int getEventTotalRevenue(long eventId, String dateType){
        return transactionRepository.getTotalEventRevenue(eventId,dateType);
    }

    @Override
    public CalculatePriceResponseDto getCalculateTransaction(CalculatePriceRequestDto calculatePriceRequestDto) {
        BigDecimal originalPrice = calculatePriceRequestDto.getOriginalPrice();
        BigDecimal rateAsBigDecimal = BigDecimal.ZERO;

        if (calculatePriceRequestDto.getVoucherId() != null) {
            Vouchers vouchers = voucherService.getVoucherById(calculatePriceRequestDto.getVoucherId());
            rateAsBigDecimal = vouchers.getRate();
        }

        BigDecimal result = originalPrice.multiply(rateAsBigDecimal);

        if (calculatePriceRequestDto.getUsePoint() == null) {
            calculatePriceRequestDto.setUsePoint(0);
        }

        BigDecimal pointsDiscount = BigDecimal.valueOf(calculatePriceRequestDto.getUsePoint());
        BigDecimal finalPrice = originalPrice.subtract(result).subtract(pointsDiscount);
        BigDecimal totalDiscount = result.add(pointsDiscount);

        CalculatePriceResponseDto calculatePriceResponseDto = new CalculatePriceResponseDto();
        calculatePriceResponseDto.setFinalPrice(finalPrice.setScale(2, RoundingMode.HALF_UP));
        calculatePriceResponseDto.setTotalDiscount(totalDiscount.setScale(2, RoundingMode.HALF_UP));
        calculatePriceResponseDto.setOriginalPrice(originalPrice.setScale(2, RoundingMode.HALF_UP));

        return calculatePriceResponseDto;
    }
}
