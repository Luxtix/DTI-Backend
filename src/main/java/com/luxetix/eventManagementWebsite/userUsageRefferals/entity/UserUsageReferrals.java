package com.luxetix.eventManagementWebsite.userUsageRefferals.entity;


import com.luxetix.eventManagementWebsite.refferals.entity.Referrals;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Data
@Table(name = "user_usage_referrals")
public class UserUsageReferrals {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_usage_referrals_id_gen")
    @SequenceGenerator(name = "user_usage_referrals_id_gen", sequenceName = "user_usage_referrals_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "referral_id", nullable = false)
    private Referrals referrals;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;


    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }
}
