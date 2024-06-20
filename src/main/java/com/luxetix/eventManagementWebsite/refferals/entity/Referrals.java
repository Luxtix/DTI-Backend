package com.luxetix.eventManagementWebsite.refferals.entity;


import com.luxetix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "referrals")
public class Referrals {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "referrals_id_gen")
    @SequenceGenerator(name = "referrals_id_gen", sequenceName = "referrals_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "code", length = 255)
    private String code;

    @NotNull(message = "User id is required")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @OneToMany(mappedBy = "referrals",cascade = CascadeType.ALL)
    private Set<UserUsageReferrals> userUsageRefferals = new LinkedHashSet<>();



}
