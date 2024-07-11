package com.luxtix.eventManagementWebsite.Transactions.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculatePriceResponseDto {

    private BigDecimal finalPrice;
    private BigDecimal totalDiscount;
    private BigDecimal originalPrice;
}
