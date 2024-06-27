package com.luxetix.eventManagementWebsite.events.services.impl;

import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.categories.Categories;
import com.luxetix.eventManagementWebsite.categories.repository.CategoryRepository;
import com.luxetix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxetix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxetix.eventManagementWebsite.eventReviews.repository.EventReviewsRepository;
import com.luxetix.eventManagementWebsite.events.dao.EventDetailDao;
import com.luxetix.eventManagementWebsite.events.dao.EventListDao;
import com.luxetix.eventManagementWebsite.events.dto.EventDetailDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.events.repository.EventRepository;
import com.luxetix.eventManagementWebsite.events.services.EventService;
import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.exceptions.InputException;
import com.luxetix.eventManagementWebsite.oganizer.repository.OrganizerRepository;
import com.luxetix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxetix.eventManagementWebsite.tickets.dto.TicketDto;
import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxetix.eventManagementWebsite.tickets.repository.TicketRepository;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import com.luxetix.eventManagementWebsite.vouchers.dao.VoucherDao;
import com.luxetix.eventManagementWebsite.vouchers.dto.VoucherDto;
import com.luxetix.eventManagementWebsite.vouchers.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Log
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private final CloudinaryService cloudinaryService;


    private final TicketRepository ticketRepository;
    private final UserRepository  userRepository;
    private final CategoryRepository categoryRepository;

    private final VoucherRepository voucherRepository;

    private final EventReviewsRepository eventReviewsRepository;

    public EventServiceImpl(EventRepository eventRepository, OrganizerRepository organizerRepository, CloudinaryService cloudinaryService, TicketRepository ticketRepository, UserRepository userRepository, CategoryRepository categoryRepository, VoucherRepository voucherRepository, EventReviewsRepository eventReviewsRepository) {
        this.eventRepository = eventRepository;
        this.cloudinaryService = cloudinaryService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.voucherRepository = voucherRepository;
        this.eventReviewsRepository = eventReviewsRepository;
    }



    @Transactional
    @Override
    public Events addNewEvent(NewEventRequestDto event) {
        Categories category = categoryRepository.findById(event.getCategory()).orElseThrow(() -> new DataNotFoundException("Category with ID " + event.getCategory() + " is not found"));
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("You are not logged in yet"));

        Events newEvent = event.toEntity();
        newEvent.setEventImage(cloudinaryService.uploadFile(event.getImage(),"folder_1"));
        if(newEvent.getEventImage() == null) {
            throw new InputException("Image Event can not be uploaded");
        }
        newEvent.setUsers(userData);
        newEvent.setCategories(category);
        Tickets newTicket = new Tickets();
        newTicket.setQty(event.getTicketQty());
        newTicket.setPrice(event.getTicketPrice());
        newTicket.setName(event.getTicketName());
        ticketRepository.save(newTicket);
        return newEvent;
    }

    @Override
    public Page<EventListDao> getAllEvent(String categoryName, String cityName, String eventName, Boolean eventType, Boolean isOnline, Boolean isFavorite,int page, int page_size) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        Pageable pageable = PageRequest.of(page, page_size);
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("You are not logged in yet"));
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
            eventData.setEventImage(allEventData.getEventImage());
            eventData.setCategoryName(allEventData.getCategoryName());
            eventData.setCityName(allEventData.getCityName());
            eventData.setTicketPrice(allEventData.getTicketPrice());
            eventData.setVenueName(allEventData.getVenueName());
            eventData.setCategoryName(allEventData.getCategoryName());
            eventData.setOnline(allEventData.getIsOnline());
            eventData.setFavorite(allEventData.getIsFavorite());
            eventData.setFavoriteCounts(allEventData.getFavoriteCount());
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
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("You are not logged in yet"));
        EventDetailDao data =  eventRepository.getEventById(userData.getId(),id);
        List<TicketDao> ticketData = ticketRepository.getEventTicket(id);
        List<VoucherDao> voucherData = voucherRepository.getEventVoucher(id,isReferrals);
        List<EventReviewsDao> reviewsData = eventReviewsRepository.getEventReviews(id);
        EventDetailDtoResponse eventDetail = new EventDetailDtoResponse();
        eventDetail.setId(data.getEventId());
        eventDetail.setDescription(data.getDescription());
        eventDetail.setEventDate(data.getEventDate());
        eventDetail.setAddress(data.getAddress());
        eventDetail.setEventName(data.getEventName());
        eventDetail.setCityName(data.getCityName());
        eventDetail.setIsOnline(data.getIsOnline());
        eventDetail.setIsFavorite(data.getIsFavorite());
        eventDetail.setIsPaid(data.getIsPaid());
        eventDetail.setEventImage(data.getEventImage());
        eventDetail.setVenueName(data.getVenueName());
        eventDetail.setStartTime(data.getStartTime());
        eventDetail.setEndTime(data.getEndTime());
        eventDetail.setOrganizerName(data.getOrganizerName());
        eventDetail.setOrganizerAvatar(data.getOrganizerAvatar());
        eventDetail.setFavoriteCounts(data.getFavoriteCounts());
        for(TicketDao ticket : ticketData){
            TicketDto newTicket = new TicketDto();
            newTicket.setId(ticket.getTicketId());
            newTicket.setName(ticket.getTicketName());
            newTicket.setPrice(ticket.getTicketPrice());
            newTicket.setQty(ticket.getTicketQuantity());
            ticketList.add(newTicket);
        }
        eventDetail.setTickets(ticketList);

        for(VoucherDao voucher : voucherData){
            VoucherDto newVoucher = new VoucherDto();
            newVoucher.setVoucherId(voucher.getVoucherId());
            newVoucher.setVoucherName(voucher.getVoucherName());
            newVoucher.setVoucherRate(voucher.getVoucherRate());
            newVoucher.setStartDate(voucher.getStartDate());
            newVoucher.setEndDate(voucher.getEndDate());
            newVoucher.setVoucherLimit(voucher.getVoucherLimit());
            voucherList.add(newVoucher);
        }
        eventDetail.setVouchers(voucherList);
        eventDetail.setReviews(reviewsData);
        return eventDetail;
    }
}
