package com.luxetix.eventManagementWebsite.vouchers.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class VoucherDto {
    private long voucherId;
    private String voucherName;
    private double voucherRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private int voucherLimit;
}
