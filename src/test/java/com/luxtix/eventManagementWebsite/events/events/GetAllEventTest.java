package com.luxtix.eventManagementWebsite.events.events;

import com.luxtix.eventManagementWebsite.events.dao.EventListDao;
import com.luxtix.eventManagementWebsite.events.repository.EventRepository;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

public class GetAllEventTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private EventRepository eventRepository;

    public void getAllEventWhenAllFilterAreNull(){
        String email = "test@gmail.com";
        String categoryName = null;
        String cityName = null;
        String eventName = null;
        Boolean eventType = null;
        Boolean isOnline = null;
        Boolean isFavorite = null;
        int page = 0;
        int pageSize = 10;

        EventListDao dao = Mockito.mock(EventListDao.class);
        Page<EventListDao> eventList = new PageImpl<>(new ArrayList<>());
        Users user = new Users();
        user.setId(1L);
        user.setEmail(email);
        Pageable pageable = PageRequest.of(page, pageSize);
        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
        Mockito.when(eventRepository.getAllEventWithFilter(1L,categoryName,eventName,cityName, eventType, isOnline, isFavorite,pageable)).thenReturn(eventList);

    }

}
