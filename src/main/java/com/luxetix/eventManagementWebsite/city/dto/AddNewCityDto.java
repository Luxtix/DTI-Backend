package com.luxetix.eventManagementWebsite.city.dto;
import com.luxetix.eventManagementWebsite.city.Cities;
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
