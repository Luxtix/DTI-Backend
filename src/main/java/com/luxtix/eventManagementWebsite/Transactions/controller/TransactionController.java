package com.luxtix.eventManagementWebsite.Transactions.controller;


import com.luxtix.eventManagementWebsite.Transactions.dto.GetTransactionResponseDto;
import com.luxtix.eventManagementWebsite.Transactions.dto.TransactionRequestDto;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.Transactions.service.TransactionService;
import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.response.Response;
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
    public ResponseEntity<Response<Transactions>> getEventById(@RequestBody TransactionRequestDto data){
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("Transaction added successfully", transactionService.newTransaction(data,email));
    }

    @GetMapping
    public ResponseEntity<Response<List<GetTransactionResponseDto>>> getAllTransaction(){
        var claims = Claims.getClaimsFromJwt();
        var userId = (long) claims.get("id");
        return Response.successfulResponse("All transactions fetched successfully", transactionService.getAllTransactions(userId));

    }
}
