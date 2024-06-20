package com.luxetix.eventManagementWebsite.pointHistory;


import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "pointshistory")
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pointshistory_id_gen")
    @SequenceGenerator(name = "pointshistory_id_gen", sequenceName = "pointshistory_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;


    @Column(name = "total_point", nullable = false)
    private int totalPoint;


    @Column(name = "expiration_date")
    private LocalDate expirationDate;

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
