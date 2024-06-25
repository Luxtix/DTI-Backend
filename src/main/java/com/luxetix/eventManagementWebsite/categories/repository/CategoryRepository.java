package com.luxetix.eventManagementWebsite.categories.repository;

import com.luxetix.eventManagementWebsite.categories.Categories;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends JpaRepository<Categories,Long> {
}
