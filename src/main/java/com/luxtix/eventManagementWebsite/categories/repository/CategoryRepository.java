package com.luxtix.eventManagementWebsite.categories.repository;

import com.luxtix.eventManagementWebsite.categories.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends JpaRepository<Categories,Long> {
}
