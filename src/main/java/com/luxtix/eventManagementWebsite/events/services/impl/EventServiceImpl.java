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
import com.luxtix.eventManagementWebsite.vouchers.dao.VoucherDao;
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

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


@Log
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CloudinaryService cloudinaryService;

    @Resource
    private Cloudinary cloudinary;
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
            Tickets newTicket = new Tickets();
            newTicket.setEvents(newEvent);
            newTicket.setName(ticketData.getName());
            newTicket.setPrice(ticketData.getPrice());
            newTicket.setQty(ticketData.getQty());
            ticketService.addNewTicket(newTicket);
        }
        for(NewEventRequestDto.VoucherEventDto voucherData : event.getVouchers()){
            Vouchers newVoucher = new Vouchers();
            newVoucher.setEvents(newEvent);
            newVoucher.setName(voucherData.getName());
            newVoucher.setRate(voucherData.getRate());
            newVoucher.setVoucherLimit(voucherData.getQty());
            newVoucher.setStartDate(voucherData.getStartDate());
            newVoucher.setEndDate(voucherData.getEndDate());
            newVoucher.setReferralOnly(voucherData.getReferralOnly());
            voucherService.addNewVoucher(newVoucher);
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
    public EventDetailDtoResponse getEventById(long id) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        var isReferrals = (Boolean) claims.get("isReferral");
        List<TicketDto> ticketList = new ArrayList<>();
        List<VoucherDto> voucherList = new ArrayList<>();
        Users userData = userService.getUserByEmail(email);
        EventDetailDao data =  eventRepository.getEventById(userData.getId(),id);
        List<TicketDao> ticketData = ticketService.getEventTicket(id);
        List<VoucherDao> voucherData = voucherService.getEventVoucher(id,isReferrals);
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
        for(TicketDao ticket : ticketData){
            TicketDto newTicket = new TicketDto();
            newTicket.setId(ticket.getTicketId());
            newTicket.setName(ticket.getTicketName());
            newTicket.setPrice(ticket.getTicketPrice());
            newTicket.setQty(ticket.getTicketQuantity());
            newTicket.setRemainingQty(ticket.getRemainingQty());
            ticketList.add(newTicket);
        }
        eventDetail.setTickets(ticketList);

        for(VoucherDao voucher : voucherData){
            VoucherDto newVoucher = new VoucherDto();
            newVoucher.setId(voucher.getVoucherId());
            newVoucher.setVoucherName(voucher.getVoucherName());
            newVoucher.setVoucherRate(voucher.getVoucherRate());
            newVoucher.setStartDate(voucher.getStartDate());
            newVoucher.setEndDate(voucher.getEndDate());
            newVoucher.setVoucherLimit(voucher.getVoucherLimit());
            newVoucher.setRemainingVoucherLimit(voucher.getRemainingVoucherLimit());
            newVoucher.setReferralOnly(voucher.getReferralOnly());
            voucherList.add(newVoucher);
        }
        eventDetail.setVouchers(voucherList);
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
        if(!data.getName().isEmpty()){
            eventData.setName(data.getName());
        }
        if(data.getCategory() != null){
            Categories categories = new Categories();
            categories.setId(data.getCategory());
            eventData.setCategories(categories);
        }
        if(data.getIsOnline() != null){
            eventData.setIsOnline(data.getIsOnline());
        }
        if(data.getEventDate() != null){
            eventData.setEventDate(data.getEventDate());
        }
        if(data.getStartTime() != null){
            eventData.setStartTime(data.getStartTime());
        }
        if(data.getEndTime() != null){
            eventData.setEndTime(data.getEndTime());
        }
        if(data.getVenue().isEmpty()){
            eventData.setVenueName(data.getVenue());
        }
        if(data.getAddress().isEmpty()){
            eventData.setAddress(data.getAddress());
        }
        if(data.getIsPaid() != null){
            eventData.setIsPaid(data.getIsPaid());
        }
        if(data.getCity() != null){
            Cities city = new Cities();
            city.setId(data.getCity());
            eventData.setCities(city);
        }
        if(data.getDescription().isEmpty()){
            eventData.setDescriptions(data.getDescription());
        }
        if(!image.isEmpty()){
            eventData.setEventImage(cloudinaryService.uploadFile(image,"folder_luxtix"));
        }
        eventRepository.save(eventData);
        for(UpdateEventRequestDto.TicketEventUpdateDto ticketData : data.getTickets()){
            if(ticketData.getId() == null){
                Tickets ticket = new Tickets();
                ticket.setName(ticketData.getName());
                ticket.setPrice(ticketData.getPrice());
                ticket.setQty(ticketData.getQty());
                ticketService.addNewTicket(ticket);
            }else{
                Tickets ticket = ticketService.getEventTicketById(ticketData.getId());
                if(!ticketData.getName().isEmpty()){
                    ticket.setName(ticketData.getName());
                }
                if(ticketData.getPrice() != null) {
                ticket.setPrice(ticketData.getPrice());
                }
                if(ticketData.getQty() != null) {
                    ticket.setQty(ticketData.getQty());
                }
                ticketService.addNewTicket(ticket);
            }

        }
        for(UpdateEventRequestDto.VoucherEventUpdateDto voucherData : data.getVouchers()){
            if(voucherData.getId() == null){
                Vouchers vouchers = new Vouchers();
                vouchers.setName(voucherData.getName());
                vouchers.setVoucherLimit(voucherData.getQty());
                vouchers.setEvents(eventData);
                vouchers.setRate(voucherData.getRate());
                if(voucherData.getStartDate() != null){
                    vouchers.setStartDate(voucherData.getStartDate());
                }
                if(voucherData.getEndDate() != null) {
                    vouchers.setEndDate(voucherData.getEndDate());
                }
                vouchers.setReferralOnly(voucherData.getReferralOnly());
                voucherService.addNewVoucher(vouchers);
            }else{
                    Vouchers vouchers = voucherService.getVoucherById(voucherData.getId());
                if(voucherData.getName() != null){
                    vouchers.setName(voucherData.getName());
                }
                if(voucherData.getQty() != 0){
                    vouchers.setVoucherLimit(voucherData.getQty());
                }
                if(voucherData.getRate() != null){
                    vouchers.setRate(voucherData.getRate());
                }
                if(voucherData.getStartDate() != null){
                    vouchers.setStartDate(voucherData.getStartDate());
                }
                if(voucherData.getEndDate() != null){
                    vouchers.setEndDate(voucherData.getEndDate());
                }

                if(voucherData.getReferralOnly() != null){
                    vouchers.setReferralOnly(voucherData.getReferralOnly());
                }
                voucherService.addNewVoucher(vouchers);
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
