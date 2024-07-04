package com.luxtix.eventManagementWebsite.vouchers.service;

import com.luxtix.eventManagementWebsite.vouchers.dao.VoucherDao;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;

import java.util.List;

public interface VoucherService {

    List<VoucherDao> getEventVoucher(long id, Boolean isReferral);

    Vouchers getVoucherById(long id);

    void addNewVoucher(Vouchers vouchers);
}
