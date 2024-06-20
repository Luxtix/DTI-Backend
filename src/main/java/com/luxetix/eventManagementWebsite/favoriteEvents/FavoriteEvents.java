package com.luxetix.eventManagementWebsite.favoriteEvents;


import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "favoriteEvents")
public class FavoriteEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favoriteEvents_id_gen")
    @SequenceGenerator(name = "favoriteEvents_id_gen", sequenceName = "favoriteEvents_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Events events;
}
