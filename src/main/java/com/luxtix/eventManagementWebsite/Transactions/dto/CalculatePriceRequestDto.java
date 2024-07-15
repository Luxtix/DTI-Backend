package com.luxtix.eventManagementWebsite.Transactions.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CalculatePriceRequestDto {


    private Long voucherId;

    @NotNull(message = "Original price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Original price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Original price must be a valid monetary amount")
    private BigDecimal originalPrice;


    private Integer usePoint;

}
