package com.luxetix.eventManagementWebsite.city.services.impl;

import com.luxetix.eventManagementWebsite.city.Cities;
import com.luxetix.eventManagementWebsite.city.dto.AddNewCityDto;
import com.luxetix.eventManagementWebsite.city.repository.CityRepository;
import com.luxetix.eventManagementWebsite.city.services.CityService;
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
