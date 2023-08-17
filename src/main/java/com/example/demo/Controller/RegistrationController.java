package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.IpApiDTO;
import com.example.demo.Model.User;
import com.example.demo.Service.IpApiService;
import com.example.demo.Service.IpApiServiceImpl;
import com.example.demo.Service.UserService;
import com.example.demo.Service.UserServiceImpl;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private IpApiService iPApiService;

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, HttpServletRequest request, BindingResult bindingResult){
        String clientIpAddress = request.getRemoteAddr();
        IpApiDTO ipDetails = iPApiService.getIpDetails(clientIpAddress); 
        Map<String, String> errors = new HashMap<>();

        //validation handling
        if(bindingResult.hasErrors()){ 
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        if(!isValidPassword(user.getPassword())){
            errors.put("password", "Password doesn't meet the criteria");
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        if(errors.isEmpty()){
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }     

        if(ipDetails == null || !"Canada".equalsIgnoreCase(ipDetails.getCountry())){
            return new ResponseEntity<>("User is not eligible to register", HttpStatus.FORBIDDEN);
        }


        user.setIpAddress(clientIpAddress);

        User registeredUser = userService.register(user);

        //welcome response/UUID generation
        Map<String, Object> response = new HashMap<>();
        response.put("uuid", UUID.randomUUID().toString());
        response.put("message", String.format("Welcome, %s !", user.getUsername()));
        response.put("city", ipDetails.getCity());
        response.put("user", registeredUser);

        //return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);


    }

    //password validation method
     private Boolean isValidPassword(String password){
        //check length
        if(password.length() <= 8){
            return false;
        }

        //check for 1 digit
        if(!password.matches(".*\\d.*")){
            return false;
        }

        //check for one uppercase
        if(!password.matches(".*[A-Z].*")){
            return false;
        }

        //check for atleast one special character from list
        if(!password.matches(".*[_#$%.].*")){
            return false;
        }
        return true;
    }
}
