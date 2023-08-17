package com.example.demo.Service;

import org.springframework.stereotype.Service;

import com.example.demo.Model.User;

@Service
public interface UserService{
    User register(User user);
    User findByUsername(String username);
}
