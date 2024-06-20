package com.luxetix.eventManagementWebsite.oganizer.repository;

import com.luxetix.eventManagementWebsite.oganizer.entity.Organizers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizerRepository extends JpaRepository<Organizers,Long> {
}
