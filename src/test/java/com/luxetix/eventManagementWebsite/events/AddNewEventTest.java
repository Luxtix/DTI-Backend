//package com.luxetix.eventManagementWebsite.events;
//
//
//import com.luxetix.eventManagementWebsite.categories.repository.CategoryRepository;
//import com.luxetix.eventManagementWebsite.city.repository.CityRepository;
//import com.luxetix.eventManagementWebsite.cloudinary.CloudinaryService;
//import com.luxetix.eventManagementWebsite.events.dto.NewEventRequestDto;
//import com.luxetix.eventManagementWebsite.events.entity.Events;
//import com.luxetix.eventManagementWebsite.events.repository.EventRepository;
//import com.luxetix.eventManagementWebsite.events.services.impl.EventServiceImpl;
//import com.luxetix.eventManagementWebsite.tickets.repository.TicketRepository;
//import com.luxetix.eventManagementWebsite.users.entity.Users;
//import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
//import com.luxetix.eventManagementWebsite.vouchers.repository.VoucherRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.Optional;
//import static org.junit.jupiter.api.Assertions.*;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//public class AddNewEventTest {
//
//    @Mock
//    private  CloudinaryService cloudinaryService;
//
//
//    @Mock
//    private  UserRepository userRepository;
//
//
//    @InjectMocks
//    private EventServiceImpl eventService;
//
//
//
//    @Test
//    public void addNewEventTest(){
//        MultipartFile image = mock(MultipartFile.class);
//        NewEventRequestDto eventDto = new NewEventRequestDto();
//        eventDto.setName("Test Event");
//        eventDto.setCategory(1L);
//        eventDto.setCity(1L);
//        eventDto.setIsOnline(true);
//        eventDto.setEventDate(LocalDate.now());
//        eventDto.setVenue("Test Venue");
//        eventDto.setAddress("Test Address");
//        eventDto.setDescription("Test Description");
//        eventDto.setIsPaid(true);
//        eventDto.setStartTime(LocalTime.now());
//        eventDto.setEndTime(LocalTime.now().plusHours(2));
//        eventDto.setTickets(new ArrayList<>());
//        eventDto.setVouchers(new ArrayList<>());
//
//        Users user = new Users();
//        user.setEmail("test@example.com");
//        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
//        Mockito.when(cloudinaryService.uploadFile(any(MultipartFile.class), anyString())).thenReturn("image_url");
//
//        Events result = eventService.addNewEvent(image, eventDto);
//        // Assert
//        assertNotNull(result);
//        assertEquals("Test Event", result.getName());
//
//
//
//    }
//}
