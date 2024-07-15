package com.luxtix.eventManagementWebsite.city.repository;

import com.luxtix.eventManagementWebsite.city.entity.Cities;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface CityRepository extends JpaRepository<Cities,Long>, JpaSpecificationExecutor<Cities> {
}
