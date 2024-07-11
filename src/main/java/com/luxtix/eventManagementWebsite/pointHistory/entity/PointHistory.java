package com.luxtix.eventManagementWebsite.pointHistory.entity;


import com.luxtix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Data
@Table(name = "points_history")
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "points_history_id_gen")
    @SequenceGenerator(name = "points_history_id_gen", sequenceName = "points_history_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;


    @Column(name = "total_point", nullable = false)
    private int totalPoint;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }
}
