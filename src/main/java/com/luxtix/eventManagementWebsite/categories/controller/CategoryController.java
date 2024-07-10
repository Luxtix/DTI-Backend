package com.luxtix.eventManagementWebsite.categories.controller;


import com.luxtix.eventManagementWebsite.categories.Categories;
import com.luxtix.eventManagementWebsite.categories.dto.CategoryResponseDto;
import com.luxtix.eventManagementWebsite.categories.service.CategoryService;
import com.luxtix.eventManagementWebsite.response.Response;
import jakarta.annotation.security.RolesAllowed;
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


    @RolesAllowed({"ORGANIZER","USER"})
    @GetMapping("")
    public ResponseEntity<Response<List<CategoryResponseDto>>> getAllCategory() {
        return Response.successfulResponse("All city has been fetched successfully", categoryService.getAllCategory());
    }
}
