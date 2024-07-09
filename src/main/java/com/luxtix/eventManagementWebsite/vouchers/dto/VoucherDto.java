package com.luxtix.eventManagementWebsite.vouchers.dto;

import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class VoucherDto {
    private Long voucherId;
    private String voucherName;
    private BigDecimal voucherRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer voucherLimit;
    private Integer remainingVoucherLimit;
    private Boolean referralOnly;
    private Boolean isValid;
}
