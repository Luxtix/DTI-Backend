package com.luxetix.eventManagementWebsite.events.services.impl;

import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.categories.Categories;
import com.luxetix.eventManagementWebsite.categories.repository.CategoryRepository;
import com.luxetix.eventManagementWebsite.city.Cities;
import com.luxetix.eventManagementWebsite.city.repository.CityRepository;
import com.luxetix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxetix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxetix.eventManagementWebsite.eventReviews.dto.ReviewEventRequestDto;
import com.luxetix.eventManagementWebsite.eventReviews.dto.ReviewEventResponseDto;
import com.luxetix.eventManagementWebsite.eventReviews.entity.EventReviews;
import com.luxetix.eventManagementWebsite.eventReviews.repository.EventReviewsRepository;
import com.luxetix.eventManagementWebsite.events.dao.EventDetailDao;
import com.luxetix.eventManagementWebsite.events.dao.EventListDao;
import com.luxetix.eventManagementWebsite.events.dto.EventDetailDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.dto.UpdateEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.events.repository.EventRepository;
import com.luxetix.eventManagementWebsite.events.services.EventService;
import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.exceptions.InputException;
import com.luxetix.eventManagementWebsite.tickets.dao.TicketDao;
import com.luxetix.eventManagementWebsite.tickets.dto.TicketDto;
import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxetix.eventManagementWebsite.tickets.repository.TicketRepository;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import com.luxetix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxetix.eventManagementWebsite.vouchers.dao.VoucherDao;
import com.luxetix.eventManagementWebsite.vouchers.dto.VoucherDto;
import com.luxetix.eventManagementWebsite.vouchers.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final CityRepository cityRepository;

    private final VoucherRepository voucherRepository;
    private final EventReviewsRepository eventReviewsRepository;

    public EventServiceImpl(EventRepository eventRepository, CloudinaryService cloudinaryService, TicketRepository ticketRepository, UserRepository userRepository, CategoryRepository categoryRepository, CityRepository cityRepository, VoucherRepository voucherRepository, EventReviewsRepository eventReviewsRepository) {
        this.eventRepository = eventRepository;
        this.cloudinaryService = cloudinaryService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.cityRepository = cityRepository;
        this.voucherRepository = voucherRepository;
        this.eventReviewsRepository = eventReviewsRepository;
    }



    @Transactional
    @Override
    public Events addNewEvent(MultipartFile image, NewEventRequestDto event) {
        Categories category = categoryRepository.findById(event.getCategory()).orElseThrow(() -> new DataNotFoundException("Category with ID " + event.getCategory() + " is not found"));
        Cities city = cityRepository.findById(event.getCity()).orElseThrow(() -> new DataNotFoundException("City with ID " + event.getCity() + " is not found"));
        Events newEvent = event.toEntity();
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("You are not logged in yet"));
        newEvent.setUsers(userData);
        newEvent.setEventImage(cloudinaryService.uploadFile(image,"folder_luxtix"));
        if(newEvent.getEventImage() == null) {
            throw new InputException("Image Event can not be uploaded");
        }
        eventRepository.save(newEvent);
        for(NewEventRequestDto.TicketEventDto ticketData : event.getTickets()){
            Tickets newTicket = new Tickets();
            newTicket.setEvents(newEvent);
            newTicket.setName(ticketData.getName());
            newTicket.setPrice(ticketData.getPrice());
            newTicket.setQty(ticketData.getQty());
            ticketRepository.save(newTicket);
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
            voucherRepository.save(newVoucher);
        }
        return newEvent;
    }

    @Override
    public Page<EventListDao> getAllEvent(String email, String categoryName, String cityName, String eventName, Boolean eventType, Boolean isOnline, Boolean isFavorite,int page, int page_size) {
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
        eventDetail.setPriceCategory(data.getPriceCategory());
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
        eventDetail.setReviews(reviewsData);
        return eventDetail;
    }

    @Override
    public void deleteEventById(long id) {
        eventRepository.deleteById(id);
    }


    @Transactional
    @Override
    public Events updateEvent(long id, MultipartFile image, UpdateEventRequestDto data) {
        Events eventData = eventRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Event with id " + id + " is not found"));
        if(!data.getName().isEmpty()){
            eventData.setName(data.getName());
        }
        if(data.getCategory() != 0){
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
        if(data.getCity() != 0){
            Cities city = new Cities();
            city.setId(data.getCity());
            eventData.setCities(city);
        }
        if(data.getDescription().isEmpty()){
            eventData.setDescriptions(data.getDescription());
        }
        if(!image.isEmpty()){
            eventData.setEventImage(cloudinaryService.uploadFile(image,"folder_1"));
        }
        eventRepository.save(eventData);
        for(UpdateEventRequestDto.TicketEventUpdateDto ticketData : data.getTickets()){
            Tickets ticket = ticketRepository.findById(ticketData.getId()).orElseThrow(() -> new DataNotFoundException("Ticket with id " + ticketData.getId() + " is not found"));
            if(!ticketData.getName().isEmpty()){
                ticket.setName(ticketData.getName());
            }
            ticket.setPrice(ticketData.getPrice());
            ticket.setQty(ticketData.getQty());
            ticketRepository.save(ticket);
        }
        for(UpdateEventRequestDto.VoucherEventUpdateDto voucherData : data.getVouchers()){
            System.out.println(voucherData.getId());
            Vouchers voucher = voucherRepository.findById(voucherData.getId()).orElseThrow(() -> new DataNotFoundException("Voucher with id " + voucherData.getId() + " is not found"));
            if(voucherData.getName() == null){
                voucher.setName(voucherData.getName());
            }
            if(voucherData.getQty() != 0){
                voucher.setVoucherLimit(voucherData.getQty());
            }
            voucher.setRate(voucherData.getRate());
            if(voucherData.getStartDate() != null){
                voucher.setStartDate(voucherData.getStartDate());
            }
            if(voucherData.getEndDate() != null){
                voucher.setEndDate(voucherData.getEndDate());
            }

            if(voucherData.getReferralOnly() != null){
                voucher.setReferralOnly(voucherData.getReferralOnly());
            }

            voucherRepository.save(voucher);
        }
        return eventData;
    }

    @Override
    public ReviewEventResponseDto addReview(String email, ReviewEventRequestDto data) {
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("You are not logged in yet"));
        EventReviews reviewData = new EventReviews();
        Events eventData = eventRepository.findById(data.getEventId()).orElseThrow(() -> new DataNotFoundException("Event id not found"));
        reviewData.setEvents(eventData);
        reviewData.setRating(data.getRating());
        reviewData.setUsers(userData);
        reviewData.setReviewCategory(data.getType());
        reviewData.setComment(data.getComments());
        eventReviewsRepository.save(reviewData);
        ReviewEventResponseDto resp = new ReviewEventResponseDto();
        resp.setId(reviewData.getId());
        resp.setComment(reviewData.getComment());
        resp.setType(reviewData.getReviewCategory());
        resp.setRating(reviewData.getRating());
        resp.setName(reviewData.getUsers().getFullname());
        return resp;
    }
}
