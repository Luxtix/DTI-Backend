package com.luxtix.eventManagementWebsite.vouchers.controller;


import com.luxtix.eventManagementWebsite.response.Response;
import com.luxtix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voucher")
@Validated
@Log
public class VoucherController {
    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    public ResponseEntity<Response<Vouchers>> deleteVoucher(@PathVariable("id") Long id) {
        voucherService.deleteVoucherById(id);
        return Response.successfulResponse("Voucher has been deleted successfully");
    }

}
