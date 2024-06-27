package com.luxetix.eventManagementWebsite.tickets.repository;

import com.luxetix.eventManagementWebsite.events.dao.EventDetailDao;
import com.luxetix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Tickets,Long> {
    public static final String eventTicketQuery = "SELECT t.id as ticketId, t.name as ticketName, t.price as ticketPrice, t.qty as ticketQuantity  from Tickets t left join Events e on t.events.id = e.id   where e.id = :eventId";


    @Query(value = eventTicketQuery)
    List<TicketDao> getEventTicket(@Param("eventId") Long eventId);
}
