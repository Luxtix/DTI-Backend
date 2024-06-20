package com.luxetix.eventManagementWebsite.transactionList;


import com.luxetix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Data
@Table(name = "transactionList")
public class TransactionList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactionList_id_gen")
    @SequenceGenerator(name = "transactionList_id_gen", sequenceName = "transactionList_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Events events;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transactions transactions;

    @Column(name = "price", nullable = false)
    @Min(value = 1, message = "Price must be at least 1")
    private int price;

    @Column(name = "quantity", nullable = false)
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

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
