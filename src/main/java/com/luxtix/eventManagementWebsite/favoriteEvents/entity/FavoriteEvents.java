package com.luxtix.eventManagementWebsite.favoriteEvents.entity;


import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "favorite_events")
public class FavoriteEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorite_events_id_gen")
    @SequenceGenerator(name = "favorite_events_id_gen", sequenceName = "favorite_events_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Events events;
}
