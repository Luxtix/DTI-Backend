package com.luxetix.eventManagementWebsite.Transactions.service.impl;

import com.luxetix.eventManagementWebsite.Transactions.dto.TransactionRequestDto;
import com.luxetix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxetix.eventManagementWebsite.Transactions.repository.TransactionRepository;
import com.luxetix.eventManagementWebsite.Transactions.service.TransactionService;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.pointHistory.entity.PointHistory;
import com.luxetix.eventManagementWebsite.pointHistory.repository.PointHistoryRepository;
import com.luxetix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxetix.eventManagementWebsite.transactionList.entity.TransactionList;
import com.luxetix.eventManagementWebsite.transactionList.repository.TransactionListRepository;
import com.luxetix.eventManagementWebsite.transactionList.service.TransactionListService;
import com.luxetix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxetix.eventManagementWebsite.userUsageRefferals.repository.UserUsageReferralsRepository;
import com.luxetix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import com.luxetix.eventManagementWebsite.users.service.UserService;
import com.luxetix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxetix.eventManagementWebsite.vouchers.repository.VoucherRepository;
import com.luxetix.eventManagementWebsite.vouchers.service.VoucherService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransactionServiceImpl implements TransactionService {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final TransactionListService transactionListService;
    private final UserUsageReferralsService userUsageReferralsService;
    private final VoucherService voucherService;

    private final PointHistoryService pointHistoryService;
    public TransactionServiceImpl(UserService userService, TransactionRepository transactionRepository, TransactionListService transactionListService, UserUsageReferralsService userUsageReferralsService, VoucherService voucherService, PointHistoryService pointHistoryService) {
        this.userService = userService;
        this.transactionRepository = transactionRepository;
        this.transactionListService = transactionListService;
        this.userUsageReferralsService = userUsageReferralsService;
        this.voucherService = voucherService;
        this.pointHistoryService = pointHistoryService;
    }

    @Override
    @Transactional
    public Transactions newTransaction(TransactionRequestDto data,String email) {
        Transactions newTransactions = new Transactions();
        Users userData =userService.getUserByEmail(email);
        newTransactions.setUsers(userData);
        Events event = new Events();
        event.setId(data.getEventId());
        newTransactions.setEvents(event);
        newTransactions.setTotalPrice(data.getTotalPrice());
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
        if(data.getUsePoint() != 0){
            PointHistory newPoint = new PointHistory();
            newPoint.setUsers(userData);
            newPoint.setTotalPoint(data.getUsePoint());
            pointHistoryService.addPointHistory(newPoint);
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


    public List<Transactions> getAllTransactions(long userId){
        return transactionRepository.findByUsersId(userId);
    }
}
