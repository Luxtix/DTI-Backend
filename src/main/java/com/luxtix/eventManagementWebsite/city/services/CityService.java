package com.luxtix.eventManagementWebsite.city.services;

import com.luxtix.eventManagementWebsite.city.entity.Cities;
import com.luxtix.eventManagementWebsite.city.dto.AddNewCityDto;

import java.util.List;

public interface CityService {
    List<Cities> getAllCity();

    Cities addNewCIty(AddNewCityDto data);
}
