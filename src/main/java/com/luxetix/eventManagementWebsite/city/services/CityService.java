package com.luxetix.eventManagementWebsite.city.services;

import com.luxetix.eventManagementWebsite.city.Cities;
import com.luxetix.eventManagementWebsite.city.dto.AddNewCityDto;

import java.util.List;

public interface CityService {
    List<Cities> getAllCity();

    Cities addNewCIty(AddNewCityDto data);
}
