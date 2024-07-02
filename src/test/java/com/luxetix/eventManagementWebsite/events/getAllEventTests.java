//package com.luxetix.eventManagementWebsite.events;
//
//
//import com.luxetix.eventManagementWebsite.cloudinary.CloudinaryService;
//import com.luxetix.eventManagementWebsite.events.dao.EventListDao;
//import com.luxetix.eventManagementWebsite.events.repository.EventRepository;
//import com.luxetix.eventManagementWebsite.events.services.impl.EventServiceImpl;
//import com.luxetix.eventManagementWebsite.exceptions.DataNotFoundException;
//import com.luxetix.eventManagementWebsite.tickets.repository.TicketRepository;
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
//    public void getAllEventTest(String email, String categoryName, String cityName, String eventName, Boolean eventType, Boolean isOnline, Boolean isFavorite, int page, int page_size) {
//        Pageable pageable = PageRequest.of(page, page_size);
//        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(Users));
//        eventRepository.getAllEventWithFilter(userData.getId(),categoryName,eventName,cityName, eventType, isOnline, isFavorite,pageable);
//    }
//
//}
