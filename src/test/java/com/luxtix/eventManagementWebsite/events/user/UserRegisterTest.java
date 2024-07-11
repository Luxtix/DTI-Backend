package com.luxtix.eventManagementWebsite.events.user;

import com.luxtix.eventManagementWebsite.Transactions.service.impl.TransactionServiceImpl;
import com.luxtix.eventManagementWebsite.cloudinary.CloudinaryService;
import com.luxtix.eventManagementWebsite.pointHistory.entity.PointHistory;
import com.luxtix.eventManagementWebsite.pointHistory.service.PointHistoryService;
import com.luxtix.eventManagementWebsite.pointHistory.service.impl.PointHistoryServiceImpl;
import com.luxtix.eventManagementWebsite.referrals.entity.Referrals;
import com.luxtix.eventManagementWebsite.referrals.service.ReferralService;
import com.luxtix.eventManagementWebsite.referrals.service.impl.ReferralServiceImpl;
import com.luxtix.eventManagementWebsite.userUsageRefferals.entity.UserUsageReferrals;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.impl.UserUsageReferralsServiceImpl;
import com.luxtix.eventManagementWebsite.users.dto.UserRegisterRequestDto;
import com.luxtix.eventManagementWebsite.users.entity.RolesType;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import com.luxtix.eventManagementWebsite.users.repository.UserRepository;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import com.luxtix.eventManagementWebsite.users.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

public class UserRegisterTest {


    @Mock
    private  UserRepository userRepository;


    @Mock
    private UserUsageReferralsServiceImpl userUsageReferralsService;


    @Mock
    private CloudinaryService cloudinaryService;



    @Mock
    private ReferralServiceImpl referralService;

    @Mock
    private PointHistoryServiceImpl pointHistoryService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, userUsageReferralsService, cloudinaryService,referralService,pointHistoryService, passwordEncoder);
    }

    @Test
    public void register_new_user_with_unique_email() {
        UserRegisterRequestDto userDto = new UserRegisterRequestDto();
        userDto.setDisplayName("John Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password123");
        userDto.setReferral("");

        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("encodedPassword");
        Mockito.when(referralService.addNewReferralCode(ArgumentMatchers.any(Referrals.class))).thenReturn(new Referrals());

        Users registeredUser = userService.register(userDto);

        assertNotNull(registeredUser);
        assertEquals("john.doe@example.com", registeredUser.getEmail());
    }


    @Test
    public void register_new_user_with_referrals() {
        UserRegisterRequestDto userDto = new UserRegisterRequestDto();
        userDto.setDisplayName("John Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password123");
        userDto.setReferral("0129213123");


        Users user = new Users();
        user.setId(1L);
        user.setEmail("maria123@gmail.com");
        user.setRole(RolesType.USER);
        user.setPassword("maria123");
        user.setFullname("Mammamia");



        Referrals userReferral = new Referrals();
        userReferral.setCode("0129213123");
        userReferral.setId(1L);
        user.setReferrals(userReferral);

        PointHistory pointHistory = new PointHistory();
        pointHistory.setTotalPoint(10000);
        pointHistory.setId(1L);
        pointHistory.setUsers(user);

        Users newUserRegistered = new Users();
        newUserRegistered.setEmail("john.doe@example.com");
        newUserRegistered.setId(2L);
        newUserRegistered.setReferrals(userReferral);

        UserUsageReferrals userUsageReferrals = new UserUsageReferrals();
        userUsageReferrals.setId(1L);
        userUsageReferrals.setBenefitClaim(false);
        userUsageReferrals.setUsers(newUserRegistered);
        userUsageReferrals.setReferrals(userReferral);


        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("encodedPassword");
        Mockito.when(referralService.addNewReferralCode(ArgumentMatchers.any(Referrals.class))).thenReturn(new Referrals());
        Mockito.when(referralService.findByReferralCode(userDto.getReferral())).thenReturn(userReferral);
        Referrals result = referralService.findByReferralCode(userDto.getReferral());
        Mockito.when(userRepository.findByReferrals(user.getReferrals())).thenReturn(Optional.of(user));
        pointHistoryService.addNewPointHistory(pointHistory);
        userUsageReferralsService.addNewUserUsageReferralData(userUsageReferrals);
        Mockito.verify(pointHistoryService).addNewPointHistory(pointHistory);
        Mockito.verify(userUsageReferralsService).addNewUserUsageReferralData(userUsageReferrals);
        Users registeredUser = userService.register(userDto);

        assertNotNull(registeredUser);
        assertEquals(userDto.getReferral(),result.getCode());
        assertEquals(userDto.getEmail(), registeredUser.getEmail());
    }
}
