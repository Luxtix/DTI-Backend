package com.luxtix.eventManagementWebsite.tickets.controller;

import com.luxtix.eventManagementWebsite.response.Response;
import com.luxtix.eventManagementWebsite.tickets.service.TicketService;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/ticket")
@Validated
@Log
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    public ResponseEntity<Response<Vouchers>> deleteTicket(@PathVariable("id") Long id) {
        ticketService.deleteTicketById(id);
        return Response.successfulResponse("Ticket has been deleted successfully");
    }
}
