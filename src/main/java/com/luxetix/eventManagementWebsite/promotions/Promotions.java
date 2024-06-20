package com.luxetix.eventManagementWebsite.promotions;


import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "promotions")
public class Promotions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promotions_id_gen")
    @SequenceGenerator(name = "promotions_id_gen", sequenceName = "promotions_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Events events;


    @Column(name = "discount_amount", nullable = false)
    private Integer discountAmount;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;


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
