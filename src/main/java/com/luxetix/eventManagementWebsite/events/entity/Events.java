package com.luxetix.eventManagementWebsite.events.entity;


import com.luxetix.eventManagementWebsite.categories.Categories;
import com.luxetix.eventManagementWebsite.eventReviews.entitiy.EventReviews;
import com.luxetix.eventManagementWebsite.favoriteEvents.FavoriteEvents;
import com.luxetix.eventManagementWebsite.oganizer.entity.Organizers;
import com.luxetix.eventManagementWebsite.promotions.Promotions;
import com.luxetix.eventManagementWebsite.transactionList.TransactionList;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;



@Entity
@Data
@Table(name = "events")
public class Events {

    public enum EventType {
        Paid,
        Free
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_id_gen")
    @SequenceGenerator(name = "events_id_gen", sequenceName = "events_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private Organizers organizers;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Categories categories;


    @NotBlank(message = "Event name is mandatory")
    @Column(name = "type")
    private EventType type;

    @NotBlank(message = "Event name is mandatory")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "City is mandatory")
    @Column(name = "city")
    private String city;

    @NotBlank(message = "Address for the event is mandatory")
    @Column(name = "address")
    private String address;

    @NotBlank(message = "Venue for the event is mandatory")
    @Column(name = "venue_name")
    private String venueName;

    @NotBlank(message = "Venue for the event is mandatory")
    @Column(name = "eventimage")
    private String eventImage;


    @NotBlank(message = "Event description is mandatory")
    @Column(name = "descriptions")
    private String descriptions;

    @FutureOrPresent(message = "Start date must be in the present or future")
    @Column(name = "start_date")
    private LocalDate startDate;

    @FutureOrPresent(message = "End date must be in the present or future")
    @Column(name = "end_date")
    private LocalDate endDate;

    // Assuming start time and end time are meant to be in the future as well
    @FutureOrPresent(message = "Start time must be in the present or future")
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @FutureOrPresent(message = "End time must be in the present or future")
    @Column(name = "end_time")
    private LocalDateTime endTime;


    @ColumnDefault("false")
    @Column(name = "isonline")
    private boolean isOnline;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;


    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "events",cascade = CascadeType.ALL)
    private Set<EventReviews> eventReviews = new LinkedHashSet<>();


    @OneToMany(mappedBy = "events",cascade = CascadeType.ALL)
    private Set<TransactionList> transactionLists = new LinkedHashSet<>();


    @OneToMany(mappedBy = "events",cascade = CascadeType.ALL)
    private Set<FavoriteEvents> favoriteEvents = new LinkedHashSet<>();

    @OneToMany(mappedBy = "events",cascade = CascadeType.ALL)
    private Set<Promotions> promotions = new LinkedHashSet<>();

    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }






}
