package com.luxtix.eventManagementWebsite.tickets.entity;


import com.luxtix.eventManagementWebsite.events.entity.Events;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Data
@Table(name = "tickets")
public class Tickets {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tickets_id_gen")
    @SequenceGenerator(name = "tickets_id_gen", sequenceName = "tickets_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Events  events;


    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "price", nullable = false)
    private int price;


    @Min(1)
    @Column(name = "qty", nullable = false)
    private int qty;

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
