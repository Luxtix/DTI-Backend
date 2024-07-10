package com.luxtix.eventManagementWebsite.categories.service.impl;

import com.luxtix.eventManagementWebsite.categories.Categories;
import com.luxtix.eventManagementWebsite.categories.dto.CategoryResponseDto;
import com.luxtix.eventManagementWebsite.categories.repository.CategoryRepository;
import com.luxtix.eventManagementWebsite.categories.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponseDto> getAllCategory() {
        List<Categories> categoryList = categoryRepository.findAll();
        List<CategoryResponseDto> list = new ArrayList<>();
        for(Categories categories : categoryList){
            CategoryResponseDto resp = new CategoryResponseDto();
            resp.setId(categories.getId());
            resp.setName(categories.getName());
            list.add(resp);
        }
        return list;
    }
}
