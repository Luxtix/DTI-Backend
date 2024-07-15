package com.luxtix.eventManagementWebsite.Transactions.controller;


import com.luxtix.eventManagementWebsite.Transactions.dto.*;
import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.Transactions.service.TransactionService;
import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.events.dto.EventListDtoResponse;
import com.luxtix.eventManagementWebsite.response.Response;
import com.luxtix.eventManagementWebsite.transactionList.entity.TransactionList;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Response<Transactions>> addNewTransaction(@Validated @RequestBody TransactionRequestDto data){
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.successfulResponse("Transaction added successfully", transactionService.newTransaction(data,email));
    }


    @PostMapping("/calculate")
    public ResponseEntity<Response<CalculatePriceResponseDto>> calculatePrice(@Validated @RequestBody CalculatePriceRequestDto calculatePriceRequestDto){
        return Response.successfulResponse("Transaction detail fetched successfully",transactionService.getCalculateTransaction(calculatePriceRequestDto));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Response<TransactionDetailResponseDto>> getAllTransactionDetail(@PathVariable("id") long transactionId){
        return Response.successfulResponse("Transaction detail fetched successfully", transactionService.getAllTransactionDetail(transactionId));
    }

    @GetMapping("")
    public ResponseEntity<Response<List<TransactionListResponseDto>>> getAllTransactions(@RequestParam(defaultValue = "0",required = false) int page, @RequestParam(defaultValue = "10",required = false) int size){
        var claims = Claims.getClaimsFromJwt();
        var userId = (long) claims.get("id");
        Page<TransactionListResponseDto> data = transactionService.getAllTransactions(userId,page,size);
        return Response.successfulResponseWithPage(HttpStatus.OK.value(),"All user transaction fetched successfully", data.getContent(),data.getTotalPages(),data.getTotalElements(),data.getNumber());
    }
}
