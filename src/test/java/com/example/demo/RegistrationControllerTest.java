package com.example.demo;

import java.util.Map;


import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Controller.RegistrationController;
import com.example.demo.DTO.IpApiDTO;
import com.example.demo.Model.User;
import com.example.demo.Service.IpApiService;
import com.example.demo.Service.IpApiServiceImpl;
import com.example.demo.Service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RegistrationControllerTest {
    
    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private IpApiServiceImpl ipApiService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_valid(){
        User user = new User();
        user.setUsername("John");
        user.setPassword("valid Password");

        IpApiDTO dto = new IpApiDTO();
        dto.setCountry("Canada");

        when(ipApiService.getIpDetails(anyString())).thenReturn(dto);
        when(userService.register(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = registrationController.registerUser(user, new MockHttpServletRequest(), bindingResult);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void registerUser_not_valid(){
        User user = new User();
        user.setUsername("John");
        user.setPassword("short");

        IpApiDTO dto = new IpApiDTO();
        dto.setCountry("USA");

        when(ipApiService.getIpDetails(anyString())).thenReturn(dto);
        when(userService.register(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = registrationController.registerUser(user, new MockHttpServletRequest(), bindingResult);
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("User is not eligible to register", response.getBody());
       
    }

    @Test
    void registerUser_invalidPassword() {
                
        
        User user = new User();
        user.setUsername("John");
        user.setPassword("short");  // Invalid password
    

        when(bindingResult.hasErrors()).thenReturn(false);  
    
        // Perform the registration action
        ResponseEntity<?> response = registrationController.registerUser(user, new MockHttpServletRequest(), bindingResult);
        
    
        // Validate the response
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    
        // Cast the response body to the expected type (Map/String) and check for the password error
        if(response.getBody() instanceof Map){
            Map<String, String> errors = (Map<String, String>) response.getBody();
            assertTrue(errors.containsKey("password"));
            assertEquals("Password doesn't meet the criteria.", errors.get("password"));
            System.out.println(response);
        }

        if(response.getBody() instanceof String){
            String responseBody = (String) response.getBody();
            assertEquals("User is not eligible to register", responseBody);
        }
    }



}
