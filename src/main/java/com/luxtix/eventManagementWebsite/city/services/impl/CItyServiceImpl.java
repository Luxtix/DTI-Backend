package com.luxtix.eventManagementWebsite.city.services.impl;
import com.luxtix.eventManagementWebsite.city.Cities;
import com.luxtix.eventManagementWebsite.city.dto.AddNewCityDto;
import com.luxtix.eventManagementWebsite.city.repository.CityRepository;
import com.luxtix.eventManagementWebsite.city.services.CityService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CItyServiceImpl implements CityService {
    private final CityRepository cityRepository;

    public CItyServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<Cities> getAllCity() {
        return cityRepository.findAll();
    }


    public Cities addNewCIty(AddNewCityDto data) {
        Cities newCity = data.toEntity();
        return cityRepository.save(newCity);
    }
}
