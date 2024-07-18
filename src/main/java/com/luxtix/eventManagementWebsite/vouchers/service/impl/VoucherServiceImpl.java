package com.luxtix.eventManagementWebsite.vouchers.service.impl;
import com.luxtix.eventManagementWebsite.dashboard.dto.DashboardEventDetailResponseDto;
import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.vouchers.dto.VoucherDto;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxtix.eventManagementWebsite.vouchers.repository.VoucherRepository;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public List<VoucherDto> getEventVoucher(long id, Boolean isReferral) {
        List<Vouchers> vouchers =  voucherRepository.findByEventsId(id);
        List<VoucherDto> voucherList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for(Vouchers voucher : vouchers){
            if(isVoucherValid(voucher,isReferral, currentDate)){
                VoucherDto voucherDto = new VoucherDto();
                voucherDto.setVoucherId(voucher.getId());
                voucherDto.setVoucherLimit(voucher.getVoucherLimit());
                voucherDto.setVoucherName(voucher.getName());
                voucherDto.setVoucherRate(voucher.getRate());
                voucherDto.setStartDate(voucher.getStartDate());
                voucherDto.setEndDate(voucher.getEndDate());
                voucherDto.setReferralOnly(voucher.getReferralOnly());
                int remainingVoucher = voucherRepository.getRemainingTicket(voucher.getId());
                voucherDto.setRemainingVoucherLimit(remainingVoucher);
                voucherList.add(voucherDto);
            }
        }
        return  voucherList;

    }


    private boolean isVoucherValid(Vouchers voucher, boolean isReferral, LocalDate currentDate) {
        if (isReferral) {
            return (voucher.getStartDate() == null || !voucher.getStartDate().isAfter(currentDate))
                    && (voucher.getEndDate() == null || !voucher.getEndDate().isBefore(currentDate));
        } else {
            return !voucher.getReferralOnly()
                    && (voucher.getStartDate() == null || !voucher.getStartDate().isAfter(currentDate))
                    && (voucher.getEndDate() == null || !voucher.getEndDate().isBefore(currentDate));
        }
    }

    @Override
    public Vouchers getVoucherById(Long id) {
        return voucherRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Voucher with id " + id + " is not found"));
    }

    @Override
    public void createNewVoucher(Vouchers newVoucher) {
        voucherRepository.save(newVoucher);
    }

    @Override
    public void updateVoucher(Vouchers updatedVoucher) {
        voucherRepository.save(updatedVoucher);
    }

    @Override
    public void deleteVoucherById(long id) {
        if(!voucherRepository.existsById(id)){
            throw new DataNotFoundException("Voucher with id " + id + " is not found");
        }
        voucherRepository.deleteById(id);
    }

    @Override
    public List<DashboardEventDetailResponseDto.VoucherEventDetailDto> getAllEventVoucher(long id) {
        List<Vouchers> vouchers =  voucherRepository.findByEventsId(id);
        List<DashboardEventDetailResponseDto.VoucherEventDetailDto> voucherList = new ArrayList<>();
        for(Vouchers voucher : vouchers){
            DashboardEventDetailResponseDto.VoucherEventDetailDto data = new DashboardEventDetailResponseDto.VoucherEventDetailDto();
            data.setId(voucher.getId());
            data.setQty(voucher.getVoucherLimit());
            data.setRate(voucher.getRate());
            data.setName(voucher.getName());
            data.setStartDate(voucher.getStartDate());
            data.setEndDate(voucher.getEndDate());
            data.setReferralOnly(voucher.getReferralOnly());
            voucherList.add(data);
        }
        return voucherList;
    }
}
