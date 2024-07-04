package com.luxtix.eventManagementWebsite.tickets.repository;

import com.luxtix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Tickets,Long> {
    public static final String eventTicketQuery = "SELECT t.id as ticketId, t.name as ticketName, t.price as ticketPrice, t.qty as ticketQuantity, t.qty - COALESCE(SUM(tl.qty),0) as remainingQty from Tickets t left join Events e on t.events.id = e.id left join TransactionList tl on t.id = tl.tickets.id where e.id = :eventId GROUP BY t.id";


    @Query(value = eventTicketQuery)
    List<TicketDao> getEventTicket(@Param("eventId") Long eventId);
}
