package com.luxtix.eventManagementWebsite.transactionList.entity;


import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Data
@Table(name = "transaction_list")
public class TransactionList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_list_id_gen")
    @SequenceGenerator(name = "transaction_list_id_gen", sequenceName = "transaction_list_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transactions transactions;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Tickets tickets;

    @Column(name = "price", nullable = false)
    @Min(value = 1, message = "Price must be at least 1")
    private int price;

    @Column(name = "qty", nullable = false)
    @Min(value = 1, message = "Quantity must be at least 1")
    private int qty;

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
