package com.luxetix.eventManagementWebsite.events.entity;


import com.luxetix.eventManagementWebsite.categories.Categories;
import com.luxetix.eventManagementWebsite.city.Cities;
import com.luxetix.eventManagementWebsite.eventReviews.entity.EventReviews;
import com.luxetix.eventManagementWebsite.favoriteEvents.entity.FavoriteEvents;
import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;



@Entity
@Data
@Table(name = "events")
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_id_gen")
    @SequenceGenerator(name = "events_id_gen", sequenceName = "events_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Categories categories;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private Cities cities;

    @NotBlank(message = "Event name is mandatory")
    @Column(name = "name")
    private String name;

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

    @FutureOrPresent(message = "Event date must be in the present or future")
    @Column(name = "event_date")
    private LocalDate eventDate;


    @Column(name = "start_time")
    private LocalTime startTime;


    @Column(name = "end_time")
    private LocalTime endTime;


    @ColumnDefault("false")
    @Column(name = "isonline")
    private Boolean isOnline;


    @ColumnDefault("true")
    @Column(name = "ispaid")
    private Boolean isPaid;

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
    private Set<Tickets> tickets = new LinkedHashSet<>();


    @OneToMany(mappedBy = "events",cascade = CascadeType.ALL)
    private Set<FavoriteEvents> favoriteEvents = new LinkedHashSet<>();


    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }






}
