package com.saima.Guardian_Link.service;

import guardianlink.service.*;
import guardianlink.dto.HelpRequestResponse;
import guardianlink.model.*;
import guardianlink.repository.CategoryRepository;
import guardianlink.repository.HelpRequestRepository;
import guardianlink.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HelpRequestServiceTest {
    @Mock
    private HelpRequestRepository helpRequestRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HelpRequestService helpRequestService;

    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@gmail.com");

        category = new Category();
        category.setName("Medical");
    }

    @Test
    void createRequest_ShouldCreateAndReturnResponse() {
        //prepare mock behaviour
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));
        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));
        when(helpRequestRepository.save(any(HelpRequest.class)))
                .thenAnswer(invocation->invocation.getArgument(0));

        //act call service method
        HelpRequestResponse response = helpRequestService.createRequest("Need Help", 1L, "test@gmail.com");

        //verify result
        assertNotNull(response);
        assertEquals("Need Help", response.getName());
        assertEquals("OPEN", response.getStatus());
        assertEquals("Medical", response.getCategoryName());
        assertEquals("test@gmail.com", response.getRequestedBy());

        verify(helpRequestRepository, times(1))
                .save(any(HelpRequest.class));    }

}
