package com.luxtix.eventManagementWebsite.city.dto;
import com.luxtix.eventManagementWebsite.city.entity.Cities;
import lombok.Data;


@Data
public class AddNewCityDto {
    private String name;

    public Cities toEntity() {
        Cities newCity = new Cities();
        newCity.setName(name);
        return newCity;
    }
}
