package com.luxtix.eventManagementWebsite.categories.service;

import com.luxtix.eventManagementWebsite.categories.Categories;
import com.luxtix.eventManagementWebsite.categories.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    List<CategoryResponseDto> getAllCategory();
}
