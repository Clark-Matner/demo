package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.DTO.IpApiDTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service

public class IpApiServiceImpl implements IpApiService{

    private static final String ip_url = "http://ip-api.com/json/";

    //for testing
    public IpApiServiceImpl(RestTemplate restTemplate) {
    }

    @Override
    public IpApiDTO getIpDetails(String ipAddress) {
        RestTemplate restTemplate = new RestTemplate();
        String url = ip_url + ipAddress;
        System.out.println("Constructed URL: " + url); 
        IpApiDTO response = restTemplate.getForObject(ip_url + ipAddress, IpApiDTO.class);
        return response;
    }
    
}
