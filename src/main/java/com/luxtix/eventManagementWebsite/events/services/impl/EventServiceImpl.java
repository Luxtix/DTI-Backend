package com.luxtix.eventManagementWebsite.events.services.impl;

import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.events.dto.EventDetailDtoResponse;
import com.luxtix.eventManagementWebsite.events.dto.EventListDtoResponse;
import com.luxtix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxtix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.events.repository.EventRepository;
import com.luxtix.eventManagementWebsite.events.services.EventService;
import com.luxtix.eventManagementWebsite.events.specification.EventListSpecification;
import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.exceptions.InputException;
import com.luxtix.eventManagementWebsite.favoriteEvents.service.FavoriteEventService;
import com.luxtix.eventManagementWebsite.tickets.dto.TicketDto;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxtix.eventManagementWebsite.tickets.service.TicketService;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxtix.eventManagementWebsite.vouchers.dto.VoucherDto;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


@Log
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CloudinaryService cloudinaryService;
    private final TicketService ticketService;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "svg", "webp");
    private final UserService userService;
    private final VoucherService voucherService;

    private final FavoriteEventService favoriteEventService;


    public EventServiceImpl(EventRepository eventRepository, CloudinaryService cloudinaryService, TicketService ticketService, UserService userService, VoucherService voucherService, FavoriteEventService favoriteEventService) {
        this.eventRepository = eventRepository;
        this.cloudinaryService = cloudinaryService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.voucherService = voucherService;
        this.favoriteEventService = favoriteEventService;
    }



    @Transactional
    @Override
    public Events addNewEvent(MultipartFile image, NewEventRequestDto event,String email) {
        Events newEvent = event.toEntity();
        Users userData = userService.getUserByEmail(email);
        newEvent.setUsers(userData);
        if(image == null || image.isEmpty()) {
            throw new InputException("Image file is null or empty");
        }
        var imageUrl = cloudinaryService.uploadFile(image,"folder_luxtix");
        newEvent.setEventImage(imageUrl);
        eventRepository.save(newEvent);
        for(NewEventRequestDto.TicketEventDto ticketData : event.getTickets()){
            Tickets ticket = ticketData.toEntity();
            ticket.setEvents(newEvent);
            ticketService.createNewTicket(ticket);
        }
        for(NewEventRequestDto.VoucherEventDto voucherData : event.getVouchers()){
            Vouchers voucher = voucherData.toEntity();
            voucher.setEvents(newEvent);
            voucherService.createNewVoucher(voucher);
        }
        return newEvent;
    }

    @Override
    public List<EventListDtoResponse> getAllEvent(String email, String categoryName, String cityName, String eventName, Boolean eventType, Boolean isOnline, Boolean isFavorite, int page, int page_size) {
        Pageable pageable = PageRequest.of(page, page_size);
        Users userData = userService.getUserByEmail(email);
        Specification<Events> specification = Specification.where(EventListSpecification.byEventName(eventName).and(EventListSpecification.byCategory(categoryName)).and(EventListSpecification.byCity(cityName)).and(EventListSpecification.byIsOnline(isOnline)).and(EventListSpecification.byIsFavorite(isFavorite, userData.getId())).and(EventListSpecification.byIsPaid(eventType)));
        Page<Events> events = eventRepository.findAll(specification,pageable);
        return  events.getContent().stream()
                .map(event -> convertAllEventToDto(event,userData.getId()))
                .toList();

    }

    @Override
    public EventListDtoResponse convertAllEventToDto(Events events, long userId) {
            EventListDtoResponse eventData = new EventListDtoResponse();
            eventData.setId(events.getId());
            eventData.setEventName(events.getName());
            if(events.getIsPaid()){
                eventData.setPriceCategory("Paid");
            }else{
                eventData.setPriceCategory("Free");
            }
            eventData.setDescriptions(events.getDescriptions());
            eventData.setAddress(events.getAddress());
            eventData.setEventImage(cloudinaryService.generateUrl(events.getEventImage()));
            eventData.setCategoryName(events.getCategories().getName());
            eventData.setCityName(events.getCities().getName());
            eventData.setTicketPrice(ticketService.getLowestTicketPrice(events.getId()));
            eventData.setVenueName(events.getVenueName());
            eventData.setOnline(events.getIsOnline());
            eventData.setFavorite(favoriteEventService.isEventFavorite(events.getId(),userId));
            eventData.setFavoriteCount(favoriteEventService.getFavoriteEventCount(events.getId()));
            DayOfWeek day = events.getEventDate().getDayOfWeek();
            String dayOfWeekString = day.getDisplayName(
                    java.time.format.TextStyle.FULL,
                    Locale.ENGLISH
            );
            eventData.setEventDay(dayOfWeekString);
            eventData.setEventDate(events.getEventDate());
            eventData.setStartTime(events.getStartTime());
            eventData.setEndTime(events.getEndTime());
            return eventData;
    }

    @Override
    public EventDetailDtoResponse getEventById(String email, Boolean isReferral, long id) {
        Events event = eventRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Event with id " + id + " is not found"));
        Users user = userService.getUserByEmail(email);
        LocalDate currentDate = LocalDate.now();
        List<TicketDto> ticketData = ticketService.getEventTicket(id);
        List<VoucherDto> voucherData = voucherService.getEventVoucher(id,isReferral);
        EventDetailDtoResponse eventDetail = new EventDetailDtoResponse();
        eventDetail.setId(event.getId());
        eventDetail.setDescription(event.getDescriptions());
        eventDetail.setEventDate(event.getEventDate());
        eventDetail.setAddress(event.getAddress());
        eventDetail.setEventName(event.getName());
        eventDetail.setCityName(event.getCities().getName());
        eventDetail.setIsOnline(event.getIsOnline());
        eventDetail.setIsFavorite(favoriteEventService.isEventFavorite(id,user.getId()));
        if(event.getIsPaid()){
            eventDetail.setPriceCategory("Paid");
        }else{
            eventDetail.setPriceCategory("Free");
        }
        eventDetail.setEventImage(cloudinaryService.generateUrl(event.getEventImage()));
        eventDetail.setVenueName(event.getVenueName());
        eventDetail.setStartTime(event.getStartTime());
        eventDetail.setEndTime(event.getEndTime());
        if(!event.getEventDate().isAfter(currentDate)){
            eventDetail.setIsDone(true);
        }else{
            eventDetail.setIsDone(false);
        }
        eventDetail.setOrganizerName(event.getUsers().getFullname());
        eventDetail.setOrganizerAvatar(cloudinaryService.generateUrl(event.getUsers().getAvatar()));
        eventDetail.setFavoriteCounts(favoriteEventService.getFavoriteEventCount(id));
        eventDetail.setTickets(ticketData);
        eventDetail.setVouchers(voucherData);
        return eventDetail;
    }

    @Override
    public void deleteEventById(long id) {
        Events event = eventRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Event with id " + id + " is not found"));
        eventRepository.deleteById(id);
    }


    @Transactional
    @Override
    public Events updateEvent(long id, MultipartFile image, UpdateEventRequestDto data) {
        Events eventData = eventRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Event with id " + id + " is not found"));
        Events updatedEvents = data.toEntity();
        updatedEvents.setId(eventData.getId());
        updatedEvents.setUsers(eventData.getUsers());
        updatedEvents.setCreatedAt(eventData.getCreatedAt());
        updatedEvents.setUpdatedAt(Instant.now());
        if(!image.isEmpty()){
            try {
                cloudinaryService.deleteImage(eventData.getEventImage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            updatedEvents.setEventImage(cloudinaryService.uploadFile(image,"folder_luxtix"));
        }
        eventRepository.save(updatedEvents);
        for(UpdateEventRequestDto.TicketEventUpdateDto ticketData : data.getTickets()){
            if(ticketData.getId() == null){
                Tickets tickets = ticketData.toEntity();
                tickets.setEvents(eventData);
                ticketService.createNewTicket(tickets);
            }else{
                Tickets ticket = ticketService.getEventTicketById(ticketData.getId());
                Tickets updatedTicket = ticketData.toEntity();
                updatedTicket.setEvents(ticket.getEvents());
                updatedTicket.setCreatedAt(ticket.getCreatedAt());
                updatedTicket.setUpdatedAt(Instant.now());
                ticketService.updateTicket(updatedTicket);
            }

        }
        for(UpdateEventRequestDto.VoucherEventUpdateDto voucherData : data.getVouchers()){
            if(voucherData.getId() == null){
                Vouchers voucher = voucherData.toEntity();
                voucher.setEvents(eventData);
                voucherService.createNewVoucher(voucher);
            }else{
                Vouchers vouchers = voucherService.getVoucherById(voucherData.getId());
                Vouchers updatedVoucher = voucherData.toEntity();
                updatedVoucher.setEvents(vouchers.getEvents());
                updatedVoucher.setCreatedAt(vouchers.getCreatedAt());
                updatedVoucher.setUpdatedAt(Instant.now());
                voucherService.updateVoucher(updatedVoucher);
            }
        }
        return eventData;
    }


    public boolean isValidImageExtension(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    public String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }



    @Override
    public Events getEventData(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new DataNotFoundException("Event with id " + eventId + " not found"));
    }


    public List<Events> getOrganizerEvent(long userId){
        return eventRepository.findByUsersId(userId).orElseThrow(() -> new DataNotFoundException("Event list is empty"));
    }
}
