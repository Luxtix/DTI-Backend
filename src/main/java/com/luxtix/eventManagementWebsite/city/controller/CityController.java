package com.luxtix.eventManagementWebsite.city.controller;


import com.luxtix.eventManagementWebsite.city.dto.CityResponseDto;
import com.luxtix.eventManagementWebsite.city.entity.Cities;
import com.luxtix.eventManagementWebsite.city.dto.AddNewCityDto;
import com.luxtix.eventManagementWebsite.city.services.CityService;
import com.luxtix.eventManagementWebsite.response.Response;
import jakarta.annotation.security.RolesAllowed;
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
    public ResponseEntity<Response<List<CityResponseDto>>> getAllCity(@RequestParam(value = "name",required = false) String name) {
        return Response.successfulResponse("All city has been fetched successfully", cityService.getAllCity(name));
    }

    @PostMapping("")
    @RolesAllowed({"ORGANIZER"})
    public ResponseEntity<Response<Cities>> addNewCity(@RequestBody AddNewCityDto data) {
        return Response.successfulResponse("All city has been fetched successfully", cityService.addNewCIty(data));
    }
}
