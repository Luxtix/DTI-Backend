package com.luxtix.eventManagementWebsite.events.repository;



import com.luxtix.eventManagementWebsite.events.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Events,Long>, JpaSpecificationExecutor<Events> {

    Optional<List<Events>> findByUsersId(long userId);
}
