package com.luxetix.eventManagementWebsite.events.services.impl;

import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.categories.Categories;
import com.luxetix.eventManagementWebsite.categories.repository.CategoryRepository;
import com.luxetix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxetix.eventManagementWebsite.events.dao.EventListDao;
import com.luxetix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxetix.eventManagementWebsite.events.entity.EventType;
import com.luxetix.eventManagementWebsite.events.entity.Events;
import com.luxetix.eventManagementWebsite.events.repository.EventRepository;
import com.luxetix.eventManagementWebsite.events.services.EventService;
import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
import com.luxetix.eventManagementWebsite.exceptions.InputException;
import com.luxetix.eventManagementWebsite.oganizer.repository.OrganizerRepository;
import com.luxetix.eventManagementWebsite.tickets.entity.Tickets;
import com.luxetix.eventManagementWebsite.tickets.repository.TicketRepository;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import lombok.extern.java.Log;
import org.apache.catalina.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Log
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private final OrganizerRepository organizerRepository;
    private final CloudinaryService cloudinaryService;


    private final TicketRepository ticketRepository;
    private final UserRepository  userRepository;
    private final CategoryRepository categoryRepository;

    public EventServiceImpl(EventRepository eventRepository, OrganizerRepository organizerRepository, CloudinaryService cloudinaryService, TicketRepository ticketRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.organizerRepository = organizerRepository;
        this.cloudinaryService = cloudinaryService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

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
    public List<GetEventListDtoResponse> getAllEvent(List<String> categoryList, String cityName, String eventName, String eventType, Boolean isOnline, Boolean isFavorite,int page, int page_size) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        Pageable pageable = PageRequest.of(page, page_size);
        Users userData = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("You are not logged in yet"));
        if (categoryList.isEmpty()) {
            List<Categories> categoryListData = categoryRepository.findAll();
            System.out.println(categoryListData);
            for(Categories categoryData : categoryListData){
                categoryList.add(categoryData.getName());
            }
        }
        System.out.println(categoryList);
        List<EventListDao> dataList = eventRepository.getAllEventWithFilter(userData.getId(),categoryList,eventName,cityName, eventType, isOnline, isFavorite,pageable);
        List<GetEventListDtoResponse> resp = new ArrayList<>();
        for(EventListDao allEventData : dataList){
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
            eventData.setTicketName(allEventData.getTicketName());
            eventData.setVenueName(allEventData.getVenueName());
            eventData.setCategoryName(allEventData.getCategoryName());
            eventData.setOnline(allEventData.getIsOnline());
            eventData.setFavorite(allEventData.getIsFavorite());
            eventData.setFavoritesCount(allEventData.getFavoriteCount());
            eventData.setStartDate(allEventData.getStartDate());
            eventData.setEndDate(allEventData.getEndDate());
            eventData.setStartTime(allEventData.getStartTime());
            eventData.setEndTime(allEventData.getEndTime());
            resp.add(eventData);
        }
        return resp;
    }

    @Override
    public Events getEventById(long id) {
        return eventRepository.findEventsById(id).orElseThrow(() -> new DataNotFoundException("Event not found"));
    }
}
