package com.luxetix.eventManagementWebsite.Transactions.service.impl;

import com.luxetix.eventManagementWebsite.Transactions.dto.TransactionRequestDto;
import com.luxetix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxetix.eventManagementWebsite.Transactions.repository.TransactionRepository;
import com.luxetix.eventManagementWebsite.Transactions.service.TransactionService;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.pointHistory.entity.PointHistory;
import com.luxetix.eventManagementWebsite.pointHistory.repository.PointHistoryRepository;
import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxetix.eventManagementWebsite.transactionList.entity.TransactionList;
import com.luxetix.eventManagementWebsite.transactionList.repository.TransactionListRepository;
import com.luxetix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxetix.eventManagementWebsite.userUsageRefferals.repository.UserUsageReferralsRepository;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import com.luxetix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxetix.eventManagementWebsite.vouchers.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransactionServiceImpl implements TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionListRepository transactionListRepository;
    private final UserUsageReferralsRepository userUsageReferralsRepository;
    private final VoucherRepository voucherRepository;

    private final PointHistoryRepository pointHistoryRepository;
    public TransactionServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository, TransactionListRepository transactionListRepository, UserUsageReferralsRepository userUsageReferralsRepository, VoucherRepository voucherRepository, PointHistoryRepository pointHistoryRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transactionListRepository = transactionListRepository;
        this.userUsageReferralsRepository = userUsageReferralsRepository;
        this.voucherRepository = voucherRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Override
    @Transactional
    public Transactions newTransaction(TransactionRequestDto data,String email) {
        Transactions newTransactions = new Transactions();
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        newTransactions.setUsers(userData);
        Events event = new Events();
        event.setId(data.getEventId());
        newTransactions.setEvents(event);
        newTransactions.setTotalPrice(data.getTotalPrice());
        newTransactions.setTotalQty(data.getTotalQty());
        Vouchers voucher = new Vouchers();
        if(data.getVoucherId() != null){
            voucher.setId(data.getVoucherId());
            Vouchers voucherData = voucherRepository.findById(data.getVoucherId()).orElseThrow(() -> new DataNotFoundException("Voucher not found"));
            if(voucherData.getReferralOnly()){
                UserUsageReferrals referralUsageData = userUsageReferralsRepository.findByUsersId(userData.getId()).orElseThrow(() -> new DataNotFoundException("User usage history with user id " + userData.getId() + " is not found"));
                referralUsageData.setBenefitClaim(true);
                userUsageReferralsRepository.save(referralUsageData);
            }
            newTransactions.setVouchers(voucher);
        }
        transactionRepository.save(newTransactions);
        if(data.getUsePoint() != 0){
            PointHistory newPoint = new PointHistory();
            newPoint.setUsers(userData);
            newPoint.setTotalPoint(data.getUsePoint());
            pointHistoryRepository.save(newPoint);
        }
        for(TransactionRequestDto.TransactionTicketDto ticketData: data.getTickets()){
            TransactionList transactionTicketData = new TransactionList();
            Tickets ticket = new Tickets();
            ticket.setId(ticketData.getTicketId());
            transactionTicketData.setTickets(ticket);
            transactionTicketData.setQty(ticketData.getQty());
            transactionTicketData.setPrice(ticketData.getPrice());
            transactionTicketData.setTransactions(newTransactions);
            transactionListRepository.save(transactionTicketData);
        }
        return newTransactions;
    }


    public List<Transactions> getAllTransactions(long userId){
        return transactionRepository.findByUsersId(userId);
    }
}
