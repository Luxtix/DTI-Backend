package com.luxtix.eventManagementWebsite.vouchers.service;

import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventDetailResponseDto;
import com.luxtix.eventManagementWebsite.vouchers.dto.VoucherDto;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;

import java.util.List;

public interface VoucherService {

    List<VoucherDto> getEventVoucher(long id, Boolean isReferral);

    Vouchers getVoucherById(Long id);

    void createNewVoucher(Vouchers newVoucher);

    void updateVoucher(Vouchers updatedVoucher);

    void deleteVoucherById(long id);

    List<DashboardEventDetailResponseDto.VoucherEventDetailDto> getAllEventVoucher(long id);
}
