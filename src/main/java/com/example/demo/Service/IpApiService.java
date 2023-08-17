package com.example.demo.Service;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.IpApiDTO;

@Service
public interface IpApiService {
    IpApiDTO getIpDetails(String ipAddress);
}
