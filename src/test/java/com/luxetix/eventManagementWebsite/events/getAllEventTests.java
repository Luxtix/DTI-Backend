//package com.luxetix.eventManagementWebsite.events;
//
//
//import com.luxetix.eventManagementWebsite.cloudinary.CloudinaryService;
//import com.luxetix.eventManagementWebsite.events.dao.EventListDao;
//import com.luxetix.eventManagementWebsite.events.dto.GetEventListDtoResponse;
//import com.luxetix.eventManagementWebsite.events.entity.Events;
//import com.luxetix.eventManagementWebsite.events.repository.EventRepository;
//import com.luxetix.eventManagementWebsite.events.services.impl.EventServiceImpl;
//import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
//import com.luxetix.eventManagementWebsite.tickets.repository.TicketRepository;
//import com.luxetix.eventManagementWebsite.users.entity.RolesType;
//import com.luxetix.eventManagementWebsite.users.entity.Users;
//import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//public class getAllEventTests {
//
//
//    @Mock
//    private EventRepository eventRepository;
//
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private EventServiceImpl eventService;
//
//
//    @Test
//    public void getAllEventTest() {
//        String email = "maria123@gmail.com";
//        Users userData = new Users();
//        userData.setId(1L);
//        userData.setEmail(email);
//        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(userData));
//
//        String categoryName = "Category";
//        String cityName = "City";
//        String eventName = "EventName";
//        Boolean eventType = true;
//        Boolean isOnline = true;
//        Boolean isFavorite = true;
//        int page = 0;
//        int pageSize = 10;
//        Pageable pageable = PageRequest.of(page, pageSize);
//        eventRepository.getAllEventWithFilter(userData.getId(),categoryName,eventName,cityName, eventType, isOnline, isFavorite,pageable);
//    }
//
//}
