package com.luxtix.eventManagementWebsite.Transactions.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CalculatePriceRequestDto {

    private Long voucherId;


    private BigDecimal originalPrice;


    private Integer usePoint;

}
