package com.luxetix.eventManagementWebsite.categories.service.impl;

import com.luxetix.eventManagementWebsite.categories.Categories;
import com.luxetix.eventManagementWebsite.categories.repository.CategoryRepository;
import com.luxetix.eventManagementWebsite.categories.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Categories> getAllCategory() {
        return categoryRepository.findAll();
    }
}
