package com.luxtix.eventManagementWebsite.city.services.impl;
import com.luxtix.eventManagementWebsite.categories.Categories;
import com.luxtix.eventManagementWebsite.categories.dto.CategoryResponseDto;
import com.luxtix.eventManagementWebsite.city.dto.CityResponseDto;
import com.luxtix.eventManagementWebsite.city.entity.Cities;
import com.luxtix.eventManagementWebsite.city.dto.AddNewCityDto;
import com.luxtix.eventManagementWebsite.city.repository.CityRepository;
import com.luxtix.eventManagementWebsite.city.services.CityService;
import com.luxtix.eventManagementWebsite.city.specification.CitySpecification;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.events.specification.EventListSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<CityResponseDto> getAllCity(String name) {
        Specification<Cities> specification = Specification.where(CitySpecification.byCityName(name));
        List<Cities> cities = cityRepository.findAll(specification);
        List<CityResponseDto> list = new ArrayList<>();
        for(Cities city : cities){
            CityResponseDto resp = new CityResponseDto();
            resp.setId(city.getId());
            resp.setName(city.getName());
            list.add(resp);
        }
        return list;
    }


    public Cities addNewCIty(AddNewCityDto data) {
        Cities newCity = data.toEntity();
        return cityRepository.save(newCity);
    }
}
