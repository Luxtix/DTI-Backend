package com.luxtix.eventManagementWebsite.events.events;


import com.cloudinary.Cloudinary;
import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.events.dto.NewEventRequestDto;
import com.luxtix.eventManagementWebsite.events.entity.Events;
import com.luxtix.eventManagementWebsite.events.repository.EventRepository;
import com.luxtix.eventManagementWebsite.events.services.impl.EventServiceImpl;
import com.luxtix.eventManagementWebsite.exceptions.InputException;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class AddNewEventTest {

    @Mock
    private  CloudinaryService cloudinaryService;


    @Mock
    private Cloudinary cloudinary;

    @Mock
    private  EventRepository userRepository;

    @Mock
    private UserService userService;


    @InjectMocks
    private EventServiceImpl eventService;



    @Test
    public void addNewEventTest(){
        MultipartFile image = mock(MultipartFile.class);
        List<NewEventRequestDto.TicketEventDto> ticketList = new ArrayList<>();
        List<NewEventRequestDto.VoucherEventDto> voucherList = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            NewEventRequestDto.TicketEventDto ticketData = new NewEventRequestDto.TicketEventDto();
            ticketData.setName("mama");
            ticketData.setQty(200);
            ticketData.setPrice(20000);
            ticketList.add(ticketData);
        }
        NewEventRequestDto.VoucherEventDto voucherData = new NewEventRequestDto.VoucherEventDto();
        voucherData.setName("hahahaha");
        voucherData.setQty(100);
        BigDecimal number1 = new BigDecimal("123.45");
        voucherData.setRate(number1);
        voucherData.setReferralOnly(false);
        voucherData.setStartDate(LocalDate.now());
        voucherData.setEndDate(LocalDate.now());
        voucherList.add(voucherData);


        NewEventRequestDto eventDto = new NewEventRequestDto();
        eventDto.setName("Test Event");
        eventDto.setCategory(1L);
        eventDto.setCity(1L);
        eventDto.setIsOnline(true);
        eventDto.setEventDate(LocalDate.now());
        eventDto.setVenue("Test Venue");
        eventDto.setAddress("Test Address");
        eventDto.setDescription("Test Description");
        eventDto.setIsPaid(true);

        String startTime = "14:30:00";
        LocalTime time1 = LocalTime.parse(startTime);

        eventDto.setStartTime(time1);
        eventDto.setEndTime(time1);
        eventDto.setTickets(ticketList);
        eventDto.setVouchers(voucherList);
        String email = "test@example.com";
        Users user = new Users();

        user.setEmail("test@example.com");

        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
        Mockito.when(cloudinaryService.uploadFile(any(), any())).thenReturn("image_url");

        Events result = eventService.addNewEvent(image, eventDto,email);


        assertNotNull(result);
        assertEquals("Test Event", result.getName());
        assertEquals(1L,result.getCategories().getId());
        assertEquals(1L,result.getCities().getId());
        assertEquals(true,result.getIsOnline());
        assertEquals(LocalDate.now(),result.getEventDate());
        assertEquals("Test Venue", result.getVenueName());
        assertEquals("Test Address", result.getAddress());
        assertEquals("Test Description", result.getDescriptions());
        assertEquals(true, result.getIsPaid());
        assertEquals(ticketList,result.getTickets());
        assertEquals(time1,result.getStartTime());
        assertEquals(time1,result.getEndTime());
    }


    @Test
    public void addNewEventWhenImageIsNull(){
        MultipartFile image = null;
        List<NewEventRequestDto.TicketEventDto> ticketList = new ArrayList<>();
        List<NewEventRequestDto.VoucherEventDto> voucherList = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            NewEventRequestDto.TicketEventDto ticketData = new NewEventRequestDto.TicketEventDto();
            ticketData.setName("mama");
            ticketData.setQty(200);
            ticketData.setPrice(20000);
            ticketList.add(ticketData);
        }
        NewEventRequestDto.VoucherEventDto voucherData = new NewEventRequestDto.VoucherEventDto();
        voucherData.setName("hahahaha");
        voucherData.setQty(100);
        BigDecimal number1 = new BigDecimal("123.45");
        voucherData.setRate(number1);
        voucherData.setReferralOnly(false);
        voucherData.setStartDate(LocalDate.now());
        voucherData.setEndDate(LocalDate.now());
        voucherList.add(voucherData);


        NewEventRequestDto eventDto = new NewEventRequestDto();
        eventDto.setName("Test Event");
        eventDto.setCategory(1L);
        eventDto.setCity(1L);
        eventDto.setIsOnline(true);
        eventDto.setEventDate(LocalDate.now());
        eventDto.setVenue("Test Venue");
        eventDto.setAddress("Test Address");
        eventDto.setDescription("Test Description");
        eventDto.setIsPaid(true);

        String startTime = "14:30:00";
        LocalTime time1 = LocalTime.parse(startTime);

        eventDto.setStartTime(time1);
        eventDto.setEndTime(time1);
        eventDto.setTickets(ticketList);
        eventDto.setVouchers(voucherList);
        String email = "test@example.com";
        Users user = new Users();

        user.setEmail("test@example.com");
        InputException exception = assertThrows(InputException.class, () -> {
            eventService.addNewEvent(image, eventDto, email);
        });
        assertEquals("Image file is null or empty", exception.getMessage());


    }
}
