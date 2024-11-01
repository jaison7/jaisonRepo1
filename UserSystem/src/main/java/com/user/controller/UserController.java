package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.dto.RegistrationDto;
import com.user.service.UserService;
import com.user.sql.entity.OrderDto;
import com.user.sql.entity.User;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<User> registerUser(@RequestBody RegistrationDto registrationDto) {
    	
        User user = userService.registerUser(registrationDto);

        return ResponseEntity.ok(user);
    }
    @PostMapping("/order")
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto) {
    	
        Long orderId = userService.saveOrder(orderDto);

        return ResponseEntity.ok(orderId);
    }

    // other endpoints for user login, profile, etc.
}
