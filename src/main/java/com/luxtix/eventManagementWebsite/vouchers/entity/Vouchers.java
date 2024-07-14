package com.luxtix.eventManagementWebsite.vouchers.entity;


import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "vouchers")
public class Vouchers {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vouchers_id_gen")
    @SequenceGenerator(name = "vouchers_id_gen", sequenceName = "vouchers_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name = "rate", precision = 5, scale = 2, nullable = false)
    @DecimalMin(value = "0.01", message = "Rate must be at least 0.01")
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Events events;


    @Min(1)
    @Column(name = "voucher_limit", nullable = false)
    private int voucherLimit;


    @Column(name = "referral_only", nullable = false)
    private Boolean referralOnly;


    @FutureOrPresent(message = "Start date must be in the present or future")
    @Column(name = "start_date")
    private LocalDate startDate;

    @FutureOrPresent(message = "End date must be in the present or future")
    @Column(name = "end_date")
    private LocalDate endDate;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;


    @OneToMany(mappedBy = "vouchers",cascade = CascadeType.ALL)
    private Set<Transactions> transactions = new LinkedHashSet<>();


    @OneToMany(mappedBy = "vouchers",cascade = CascadeType.ALL)
    private Set<Transactions> transactions = new LinkedHashSet<>();

    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }
}
