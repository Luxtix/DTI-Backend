package com.luxtix.eventManagementWebsite.Transactions.controller;


import com.luxtix.eventManagementWebsite.Transactions.dto.*;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.Transactions.service.TransactionService;
import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.response.Response;
import com.luxtix.eventManagementWebsite.transactionList.entity.TransactionList;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@Validated
@Log
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("")
    @RolesAllowed({"USER"})
    public ResponseEntity<Response<Transactions>> addNewTransaction(@RequestBody TransactionRequestDto data){
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("Transaction added successfully", transactionService.newTransaction(data,email));
    }


    @PostMapping("/calculate")
    @RolesAllowed({"USER"})
    public ResponseEntity<Response<CalculatePriceResponseDto>> calculatePrice(@RequestBody CalculatePriceRequestDto calculatePriceRequestDto){
        return Response.successfulResponse("Transaction detail fetched successfully",transactionService.getCalculateTransaction(calculatePriceRequestDto));
    }

    @GetMapping("/detail/{id}")
    @RolesAllowed({"USER"})
    public ResponseEntity<Response<List<TransactionDetailResponseDto>>> getAllTransactionDetail(@PathVariable("id") long transactionId){
        return Response.successfulResponse("Transaction detail fetched successfully", transactionService.getAllTransactionDetail(transactionId));
    }

    @GetMapping("")
    @RolesAllowed({"USER"})
    public ResponseEntity<Response<List<TransactionListResponseDto>>> getAllTransactions(){
        var claims = Claims.getClaimsFromJwt();
        var userId = (long) claims.get("id");
        return Response.successfulResponse("All user transaction fetched successfully", transactionService.getAllTransactions(userId));
    }
}
