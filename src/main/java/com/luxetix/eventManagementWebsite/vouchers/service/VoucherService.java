package com.luxetix.eventManagementWebsite.vouchers.service;

import com.luxetix.eventManagementWebsite.vouchers.dao.VoucherDao;
import com.luxetix.eventManagementWebsite.vouchers.entity.Vouchers;

import java.util.List;

public interface VoucherService {

    List<VoucherDao> getEventVoucher(long id, Boolean isReferral);

    Vouchers getVoucherById(long id);

    void addNewVoucher(Vouchers vouchers);
}
