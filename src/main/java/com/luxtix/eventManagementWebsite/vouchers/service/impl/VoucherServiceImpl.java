package com.luxtix.eventManagementWebsite.vouchers.service.impl;

import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.vouchers.dao.VoucherDao;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxtix.eventManagementWebsite.vouchers.repository.VoucherRepository;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public List<VoucherDao> getEventVoucher(long id, Boolean isReferral) {
        return voucherRepository.getEventVoucher(id,isReferral);
    }

    @Override
    public Vouchers getVoucherById(long id) {
        return voucherRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Voucher with id " + id + " is not found"));
    }

    @Override
    public void addNewVoucher(Vouchers vouchers) {
        voucherRepository.save(vouchers);
    }

    @Override
    public void deleteVoucherById(long id) {
        if(!voucherRepository.existsById(id)){
            throw new DataNotFoundException("Voucher with id " + id + " is not found");
        }
        voucherRepository.deleteById(id);
    }
}
