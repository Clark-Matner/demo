package com.example.demo;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.DTO.IpApiDTO;
import com.example.demo.Service.IpApiService;
import com.example.demo.Service.IpApiServiceImpl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IpApiServiceTest {
    
    
    private IpApiServiceImpl iPservice;

    private RestTemplate restTemplate;


    @BeforeEach
    void setUp(){
        //Mockito.reset(restTemplate);
        restTemplate = mock(RestTemplate.class);
        iPservice = new IpApiServiceImpl(restTemplate);

    }

    @Test
    void getIpDetails(){
        IpApiDTO expectedResponse = new IpApiDTO();
        expectedResponse.setCity("Ashburn");
        expectedResponse.setCountry("United States");

        //String testUrl = "http://ip-api.com/json/8.8.8.8";

        lenient().when(restTemplate.getForObject(anyString(), eq(IpApiDTO.class))).thenReturn(expectedResponse);
         IpApiDTO actualResponse = iPservice.getIpDetails("8.8.8.8");

        assertEquals("United States", actualResponse.getCountry());
        assertEquals("Ashburn", actualResponse.getCity());
    }

    @Test
    void manualTestKnownIp(){

        try{
            String testUrl = "http://ip-api.com/json/8.8.8.8";
            RestTemplate localRestTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = localRestTemplate.getForEntity(testUrl, String.class);

            System.out.println("Response Status: " + responseEntity.getStatusCode());
            System.out.println("Response body: " + responseEntity.getBody());
        }catch(Exception e){
            e.printStackTrace();
            fail("Failed due to an exception" + e.getMessage());
        }

    }


}
