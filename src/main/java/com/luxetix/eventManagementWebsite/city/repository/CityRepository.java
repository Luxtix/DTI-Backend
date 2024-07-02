package com.luxetix.eventManagementWebsite.city.repository;

import com.luxetix.eventManagementWebsite.city.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CityRepository extends JpaRepository<Cities,Long> {
}
