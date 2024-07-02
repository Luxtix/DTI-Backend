package com.luxetix.eventManagementWebsite.city;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Data
@Table(name = "cities")
public class Cities {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_id_gen")
    @SequenceGenerator(name = "cities_id_gen", sequenceName = "cities_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @NotBlank(message = "City name is mandatory")
    @Column(name = "name", nullable = false)
    private String name;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }
}
