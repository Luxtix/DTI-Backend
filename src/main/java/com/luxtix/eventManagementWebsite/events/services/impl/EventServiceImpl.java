package com.luxtix.eventManagementWebsite.events.services.impl;

import com.cloudinary.Cloudinary;
import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.categories.Categories;
import com.luxtix.eventManagementWebsite.city.entity.Cities;
import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.events.dao.EventDetailDao;
import com.luxtix.eventManagementWebsite.events.dao.EventListDao;
import com.luxtix.eventManagementWebsite.events.dao.EventSummaryDao;
import com.luxtix.eventManagementWebsite.events.dto.EventDetailDtoResponse;
import com.luxtix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import com.luxtix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxtix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.events.repository.EventRepository;
import com.luxtix.eventManagementWebsite.events.services.EventService;
import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxtix.eventManagementWebsite.exceptions.InputException;
import com.luxtix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxtix.eventManagementWebsite.tickets.dto.TicketDto;
import com.luxtix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxtix.eventManagementWebsite.tickets.service.TicketService;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import com.luxtix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxtix.eventManagementWebsite.vouchers.dto.VoucherDto;
import com.luxtix.eventManagementWebsite.vouchers.service.VoucherService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.ArrayList;
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


    public EventServiceImpl(EventRepository eventRepository, CloudinaryService cloudinaryService, TicketService ticketService, UserService userService, VoucherService voucherService) {
        this.eventRepository = eventRepository;
        this.cloudinaryService = cloudinaryService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.voucherService = voucherService;
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
    public Page<EventListDao> getAllEvent(String email, String categoryName, String cityName, String eventName, Boolean eventType, Boolean isOnline, Boolean isFavorite,int page, int page_size) {
        Pageable pageable = PageRequest.of(page, page_size);
        Users userData = userService.getUserByEmail(email);
        return eventRepository.getAllEventWithFilter(userData.getId(),categoryName,eventName,cityName, eventType, isOnline, isFavorite,pageable);
    }

    @Override
    public List<GetEventListDtoResponse> convertAllEventToDto(Page<EventListDao> data) {
        List<GetEventListDtoResponse> resp = new ArrayList<>();
        for(EventListDao allEventData : data){
            GetEventListDtoResponse eventData = new GetEventListDtoResponse();
            eventData.setId(allEventData.getEventId());
            eventData.setEventName(allEventData.getEventName());
            eventData.setPriceCategory(allEventData.getPriceCategory());
            eventData.setDescriptions(allEventData.getDescriptions());
            eventData.setAddress(allEventData.getAddress());
            eventData.setEventImage(cloudinaryService.generateUrl(allEventData.getEventImage()));
            eventData.setCategoryName(allEventData.getCategoryName());
            eventData.setCityName(allEventData.getCityName());
            eventData.setTicketPrice(allEventData.getTicketPrice());
            eventData.setVenueName(allEventData.getVenueName());
            eventData.setCategoryName(allEventData.getCategoryName());
            eventData.setOnline(allEventData.getIsOnline());
            eventData.setFavorite(allEventData.getIsFavorite());
            eventData.setFavoriteCount(allEventData.getFavoriteCount());
            DayOfWeek day = allEventData.getEventDate().getDayOfWeek();
            String dayOfWeekString = day.getDisplayName(
                    java.time.format.TextStyle.FULL,
                    Locale.ENGLISH
            );
            eventData.setEventDay(dayOfWeekString);
            eventData.setEventDate(allEventData.getEventDate());
            eventData.setStartTime(allEventData.getStartTime());
            eventData.setEndTime(allEventData.getEndTime());
            resp.add(eventData);
        }
        return resp;
    }

    @Override
    public EventDetailDtoResponse getEventById(String email, Boolean isReferral, long id) {
        Users userData = userService.getUserByEmail(email);
        EventDetailDao data =  eventRepository.getEventById(userData.getId(),id);
        List<TicketDto> ticketData = ticketService.getEventTicket(id);
        List<VoucherDto> voucherData = voucherService.getEventVoucher(id,isReferral);
        EventDetailDtoResponse eventDetail = new EventDetailDtoResponse();
        eventDetail.setId(data.getEventId());
        eventDetail.setDescription(data.getDescription());
        eventDetail.setEventDate(data.getEventDate());
        eventDetail.setAddress(data.getAddress());
        eventDetail.setEventName(data.getEventName());
        eventDetail.setCityName(data.getCityName());
        eventDetail.setIsOnline(data.getIsOnline());
        eventDetail.setIsFavorite(data.getIsFavorite());
        eventDetail.setPriceCategory(data.getPriceCategory());
        eventDetail.setEventImage(data.getEventImage());
        eventDetail.setVenueName(data.getVenueName());
        eventDetail.setStartTime(data.getStartTime());
        eventDetail.setEndTime(data.getEndTime());
        eventDetail.setIsDone(data.getIsDone());
        eventDetail.setOrganizerName(data.getOrganizerName());
        eventDetail.setOrganizerAvatar(data.getOrganizerAvatar());
        eventDetail.setFavoriteCounts(data.getFavoriteCounts());
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

    public EventSummaryDao getEventSummaryData(long eventId){
        return eventRepository.getEventDataSummary(eventId);
    }

    @Override
    public Events getEventData(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new DataNotFoundException("Event with id " + eventId + " not found"));
    }


    public List<Events> getOrganizerEvent(long userId){
        return eventRepository.findByUsersId(userId).orElseThrow(() -> new DataNotFoundException("Event list is empty"));
    }
}
