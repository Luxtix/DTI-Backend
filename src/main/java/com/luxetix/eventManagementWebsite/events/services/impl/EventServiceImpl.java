package com.luxetix.eventManagementWebsite.events.services.impl;

import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.categories.Categories;
import com.luxetix.eventManagementWebsite.city.Cities;
import com.luxetix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxetix.eventManagementWebsite.eventReviews.dao.EventReviewsDao;
import com.luxetix.eventManagementWebsite.eventReviews.dto.EventReviewsDto;
import com.luxetix.eventManagementWebsite.eventReviews.dto.ReviewEventRequestDto;
import com.luxetix.eventManagementWebsite.eventReviews.dto.ReviewEventResponseDto;
import com.luxetix.eventManagementWebsite.eventReviews.entity.EventReviews;
import com.luxetix.eventManagementWebsite.eventReviews.service.EventReviewService;
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
import com.luxetix.eventManagementWebsite.tickets.service.TicketService;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.service.UserService;
import com.luxetix.eventManagementWebsite.vouchers.entity.Vouchers;
import com.luxetix.eventManagementWebsite.vouchers.dao.VoucherDao;
import com.luxetix.eventManagementWebsite.vouchers.dto.VoucherDto;
import com.luxetix.eventManagementWebsite.vouchers.service.VoucherService;
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
    private final TicketService ticketService;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "svg", "webp");
    private final UserService userService;
    private final VoucherService voucherService;
    private final EventReviewService eventReviewService;

    public EventServiceImpl(EventRepository eventRepository, CloudinaryService cloudinaryService, TicketService ticketService, UserService userService, VoucherService voucherService, EventReviewService eventReviewService) {
        this.eventRepository = eventRepository;
        this.cloudinaryService = cloudinaryService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.voucherService = voucherService;
        this.eventReviewService = eventReviewService;
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
        List<EventReviewsDto> reviewList = new ArrayList<>();
        Users userData = userService.getUserByEmail(email);
        EventDetailDao data =  eventRepository.getEventById(userData.getId(),id);
        List<TicketDao> ticketData = ticketService.getEventTicket(id);
        List<VoucherDao> voucherData = voucherService.getEventVoucher(id,isReferrals);
        List<EventReviewsDao> reviewsData = eventReviewService.getEventReviews(id);
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

        for(EventReviewsDao reviewData : reviewsData){
            EventReviewsDto newReview = new EventReviewsDto();
            newReview.setId(reviewData.getId());
            newReview.setType(reviewData.getReviewCategory());
            newReview.setReviewerName(reviewData.getReviewerName());
            newReview.setRating(reviewData.getRating());
            newReview.setComments(reviewData.getComment());
            reviewList.add(newReview);
        }
        eventDetail.setReviews(reviewList);
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
            eventData.setEventImage(cloudinaryService.uploadFile(image,"folder_luxtix"));
        }
        eventRepository.save(eventData);
        for(UpdateEventRequestDto.TicketEventUpdateDto ticketData : data.getTickets()){
            Tickets ticket = ticketService.getEventTicketById(ticketData.getId());
            if(!ticketData.getName().isEmpty()){
                ticket.setName(ticketData.getName());
            }
            ticket.setPrice(ticketData.getPrice());
            ticket.setQty(ticketData.getQty());
            ticketService.addNewTicket(ticket);
        }
        for(UpdateEventRequestDto.VoucherEventUpdateDto voucherData : data.getVouchers()){
            System.out.println(voucherData.getId());
            Vouchers voucher = voucherService.getVoucherById(voucherData.getId());
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

            voucherService.addNewVoucher(voucher);
        }
        return eventData;
    }

    @Override
    public ReviewEventResponseDto addReview(String email, ReviewEventRequestDto data) {
        Users userData = userService.getUserByEmail(email);
        EventReviews reviewData = new EventReviews();
        Events eventData = eventRepository.findById(data.getEventId()).orElseThrow(() -> new DataNotFoundException("Event id not found"));
        reviewData.setEvents(eventData);
        reviewData.setRating(data.getRating());
        reviewData.setUsers(userData);
        reviewData.setReviewCategory(data.getType());
        reviewData.setComment(data.getComments());
        eventReviewService.addNewReview(reviewData);
        ReviewEventResponseDto resp = new ReviewEventResponseDto();
        resp.setId(reviewData.getId());
        resp.setComment(reviewData.getComment());
        resp.setType(reviewData.getReviewCategory());
        resp.setRating(reviewData.getRating());
        resp.setName(reviewData.getUsers().getFullname());
        return resp;
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
}
