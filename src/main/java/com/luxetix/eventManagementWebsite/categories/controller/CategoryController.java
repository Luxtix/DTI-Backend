package com.luxetix.eventManagementWebsite.categories.controller;


import com.luxetix.eventManagementWebsite.categories.Categories;
import com.luxetix.eventManagementWebsite.categories.service.CategoryService;
import com.luxetix.eventManagementWebsite.city.Cities;
import com.luxetix.eventManagementWebsite.response.Response;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Validated
@Log
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<Response<List<Categories>>> getAllCategory() {
        return Response.successfulResponse("All city has been fetched successfully", categoryService.getAllCategory());
    }
}
