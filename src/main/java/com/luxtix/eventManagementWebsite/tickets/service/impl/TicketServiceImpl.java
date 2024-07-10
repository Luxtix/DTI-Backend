package com.luxtix.eventManagementWebsite.tickets.service.impl;

import com.luxtix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxtix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketSummaryDao;
import com.luxtix.eventManagementWebsite.tickets.dto.TicketDto;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxtix.eventManagementWebsite.tickets.repository.TicketRepository;
import com.luxtix.eventManagementWebsite.tickets.service.TicketService;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void createNewTicket(Tickets newTickets) {
        ticketRepository.save(newTickets);
    }


    @Override
    public void updateTicket(Tickets updatedTickets){
        ticketRepository.save(updatedTickets);
    }

    @Override
    public List<TicketDto> getEventTicket(long eventId) {
        List<Tickets> tickets = ticketRepository.findByEventsId(eventId);
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for(Tickets ticket : tickets){
            TicketDto newTicketDto = new TicketDto();
            newTicketDto.setId(ticket.getId());
            newTicketDto.setPrice(ticket.getPrice());
            newTicketDto.setQty(ticket.getQty());
            newTicketDto.setName(ticket.getName());
            newTicketDto.setRemainingQty(ticketRepository.getRemainingTicket(ticket.getId()));
            ticketDtoList.add(newTicketDto);

        }
        return ticketDtoList;
    }


    @Override

    public int getLowestTicketPrice(long eventId){
        return ticketRepository.getLowestTicketPrice(eventId);
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


    @Override
    public int getTicketSoldQuantity(long eventId, String dateFilter){
        return ticketRepository.getTicketSoldQuantity(eventId,dateFilter);
    }



    @Override
    public int getTotalTicketInEvent(long eventId){
        return ticketRepository.getTotalTicketInEvent(eventId);
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

                    LocalDate lastDayOfMonth = LocalDate.now(zoneId).with(TemporalAdjusters.lastDayOfMonth());
                    LocalDateTime endOfMonth = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
                    endDate = endOfMonth.atZone(zoneId).toInstant();
                    intervalTime = "1 day";
                    intervalTo = "day";
                    break;
            case "day" :
                    intervalStart = "hour";
                    zoneId = ZoneId.of("Africa/Lagos");

                    ZonedDateTime currentZonedDateTime = ZonedDateTime.now(zoneId);

                    ZonedDateTime startOfDay = currentZonedDateTime.truncatedTo(ChronoUnit.DAYS);
                    startDate = startOfDay.toInstant();

                    endDate = startOfDay.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.NANOS).toInstant();
                    intervalTime = "1 hour";
                    intervalTo = "hour";
                    break;
        }
        return ticketRepository.getTransactionTicketSummary(intervalStart,startDate.toString(),endDate.toString(),intervalTime,intervalTo,eventId);
    }
}
