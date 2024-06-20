package com.luxetix.eventManagementWebsite.tickets.repository;

import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Tickets,Long> {
}
