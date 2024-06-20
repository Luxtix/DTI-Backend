package com.luxetix.eventManagementWebsite.vouchers.repository;

import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.vouchers.Vouchers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Vouchers,Long> {
}
