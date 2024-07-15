package com.luxtix.eventManagementWebsite.city.specification;

import com.luxtix.eventManagementWebsite.city.entity.Cities;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import org.springframework.data.jpa.domain.Specification;

public class CitySpecification {
    public static Specification<Cities> byCityName(String name){
        return ((root, query, cb) -> {
            if(name== null){
                return cb.conjunction();
            }
            if(name.isEmpty()){
                return cb.equal(root.get("eventName"),"");
            }
            return cb.like(cb.lower(root.get("name")),"%" + name.toLowerCase() + "%");
        });
    }
}
