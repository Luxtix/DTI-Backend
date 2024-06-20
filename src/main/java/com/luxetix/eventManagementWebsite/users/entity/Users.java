package com.luxetix.eventManagementWebsite.users.entity;


import com.luxetix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxetix.eventManagementWebsite.eventReviews.entitiy.EventReviews;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.favoriteEvents.FavoriteEvents;
import com.luxetix.eventManagementWebsite.oganizer.entity.Organizers;
import com.luxetix.eventManagementWebsite.pointHistory.PointHistory;
import com.luxetix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxetix.eventManagementWebsite.vouchers.Vouchers;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "roles_type")
    @NotNull(message = "Role cannot be null")
    private RolesType role;


    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Column(name = "email",unique = true)
    private String email;

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    @Column(name = "username")
    private String username;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullname;


    @Column(name = "avatar", nullable = true)
    private String avatar;

    @Column(name = "phonenumber")
    private String phonenumber;


    @Column(name = "total_points")
    private Long totalPoints;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizer_id", referencedColumnName = "id")
    private Organizers organizers;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;


    @Column(name = "deleted_at")
    private Instant deletedAt;


    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
    private Set<EventReviews> eventReviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
    private Set<UserUsageReferrals> userUsageRefferals = new LinkedHashSet<>();

    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
    private Set<Transactions> transactions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
    private Set<Vouchers> vouchers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
    private Set<FavoriteEvents> favoriteEvents = new LinkedHashSet<>();


    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
    private Set<PointHistory> pointHistories = new LinkedHashSet<>();
    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }


}
