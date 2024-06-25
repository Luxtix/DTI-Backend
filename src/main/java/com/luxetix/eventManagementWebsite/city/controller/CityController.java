package com.luxetix.eventManagementWebsite.city.controller;


import com.luxetix.eventManagementWebsite.city.Cities;
import com.luxetix.eventManagementWebsite.city.dto.AddNewCityDto;
import com.luxetix.eventManagementWebsite.city.services.CityService;
import com.luxetix.eventManagementWebsite.response.Response;
import com.luxetix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@Validated
@Log
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("")
    public ResponseEntity<Response<List<Cities>>> getAllCity() {
        return Response.successfulResponse("All city has been fetched successfully", cityService.getAllCity());
    }

    @PostMapping("")
    public ResponseEntity<Response<Cities>> addNewCity(@RequestBody AddNewCityDto data) {
        return Response.successfulResponse("All city has been fetched successfully", cityService.addNewCIty(data));
    }
}
