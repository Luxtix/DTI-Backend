package com.luxtix.eventManagementWebsite.tickets.service.impl;

import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxtix.eventManagementWebsite.tickets.repository.TicketRepository;
import com.luxtix.eventManagementWebsite.tickets.service.TicketService;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void addNewTicket(Tickets tickets) {
        ticketRepository.save(tickets);
    }

    @Override
    public List<TicketDao> getEventTicket(long eventId) {
        return ticketRepository.getEventTicket(eventId);
    }

    @Override
    public Tickets getEventTicketById(long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Ticket with id " + id + " is not found"));
    }

    public void deleteTicketById(long id) {
        if(!ticketRepository.existsById(id)){
            throw new DataNotFoundException("Ticket with id " + id + " is not found");
        }
        ticketRepository.deleteById(id);
    }


    public List<TicketSummaryDao> getTicketSummaryData(long eventId, String dateType){
        String intervalStart = "";
        Instant startDate = null;
        Instant endDate = null;
        String intervalTime = "";
        String intervalTo = "";
        switch (dateType){
            case "year":
                    intervalStart = "month";
                    ZoneId indonesiaZoneId = ZoneId.of("Africa/Abidjan");
                    LocalDate now = LocalDate.now(indonesiaZoneId);

                    startDate = now.withDayOfYear(1).atStartOfDay(indonesiaZoneId).toInstant();
                    endDate = now.withDayOfYear(now.lengthOfYear()).atTime(LocalTime.MAX).atZone(indonesiaZoneId).toInstant();
                    intervalTime = "1 month";
                    intervalTo = "month";
                    break;
            case "month":
                    intervalStart = "day";
                    ZoneId zoneId = ZoneId.of("Africa/Abidjan");

                    LocalDate firstDayOfMonth = LocalDate.now(zoneId).with(TemporalAdjusters.firstDayOfMonth());
                    LocalDateTime startOfMonth = LocalDateTime.of(firstDayOfMonth, LocalTime.MIN);
                    startDate = startOfMonth.atZone(zoneId).toInstant();

                // Last day of the month
                    LocalDate lastDayOfMonth = LocalDate.now(zoneId).with(TemporalAdjusters.lastDayOfMonth());
                    LocalDateTime endOfMonth = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
                    endDate = endOfMonth.atZone(zoneId).toInstant();
                    intervalTime = "1 day";
                    intervalTo = "day";
                    break;
            case "day" :
                    intervalStart = "hour";
                    zoneId = ZoneId.of("Africa/Lagos");

                // Get the current instant in the specified timezone
                    ZonedDateTime currentZonedDateTime = ZonedDateTime.now(zoneId);

                // Truncate to the start of the day in the specified timezone
                    ZonedDateTime startOfDay = currentZonedDateTime.truncatedTo(ChronoUnit.DAYS);
                    startDate = startOfDay.toInstant();

                // Calculate the end of the day in the specified timezone
                    endDate = startOfDay.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.NANOS).toInstant();
                    intervalTime = "1 hour";
                    intervalTo = "hour";
                    break;
        }
        return ticketRepository.getTransactionTicketSummary(intervalStart,startDate.toString(),endDate.toString(),intervalTime,intervalTo,eventId);
    }
}
