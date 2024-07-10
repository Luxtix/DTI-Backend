package com.luxtix.eventManagementWebsite.events.specification;

import com.luxtix.eventManagementWebsite.categories.Categories;
import com.luxtix.eventManagementWebsite.city.entity.Cities;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.favoriteEvents.entity.FavoriteEvents;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class EventListSpecification {

    public static Specification<Events> byEventName(String eventName){
        return ((root, query, cb) -> {
            if(eventName == null){
                return cb.conjunction();
            }
            if(eventName.isEmpty()){
                return cb.equal(root.get("eventName"),"");
            }
            return cb.like(cb.lower(root.get("name")),"%" + eventName.toLowerCase() + "%");
        });
    }

    public static Specification<Events> byCategory(String category){
        return((root, query, cb) -> {
            if(category == null || category.isEmpty()){
                return cb.conjunction();
            }
            Join<Events, Categories> categoryJoin = root.join("categories", JoinType.LEFT);
            return cb.equal(cb.lower(categoryJoin.get("name")), category.toLowerCase());
        });
    }

    public static Specification<Events> byCity(String city){
        return((root, query, cb) -> {
            if(city == null || city.isEmpty()){
                return cb.conjunction();
            }
            Join<Events, Cities> cityJoin = root.join("cities", JoinType.LEFT);
            return cb.equal(cb.lower(cityJoin.get("name")), city.toLowerCase());
        });
    }

    public static Specification<Events> byIsOnline(Boolean isOnline){
        return((root, query, cb) -> {
            if(isOnline== null){
                return cb.conjunction();
            }
            return cb.equal(root.get("isOnline"), isOnline);
        });
    }

    public static Specification<Events> byIsPaid(Boolean isPaid){
        return (root, query, cb) -> {
            if (isPaid == null) {
                return cb.conjunction();
            }
            return isPaid ? cb.isTrue(root.get("isPaid")) : cb.isFalse(root.get("isPaid"));
        };
    }

    public static Specification<Events> byIsFavorite(Boolean isFavorite, Long userId) {
        return (root, query, cb) -> {
            if (isFavorite == null) {
                return cb.conjunction();
            }
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<FavoriteEvents> subRoot = subquery.from(FavoriteEvents.class);
            subquery.select(cb.literal(1L))
                    .where(cb.equal(subRoot.get("events").get("id"), root.get("id")),
                            cb.equal(subRoot.get("users").get("id"), userId));

            if (isFavorite) {
                return cb.exists(subquery);
            } else {
                return cb.not(cb.exists(subquery));
            }
        };
    }


}
