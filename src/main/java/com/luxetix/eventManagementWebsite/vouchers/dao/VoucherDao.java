package com.luxetix.eventManagementWebsite.vouchers.dao;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public interface VoucherDao {
    long getVoucherId();
    String getVoucherName();

    double getVoucherRate();

    LocalDate getStartDate();

    LocalDate getEndDate();

    int getVoucherLimit();

    boolean getReferralOnly();
}
