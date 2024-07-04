package com.luxtix.eventManagementWebsite.vouchers.dao;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public interface VoucherDao {
    long getVoucherId();
    String getVoucherName();

    double getVoucherRate();

    LocalDate getStartDate();

    LocalDate getEndDate();

    int getVoucherLimit();

    int getRemainingVoucherLimit();

    boolean getReferralOnly();
}
