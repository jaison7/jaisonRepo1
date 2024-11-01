package com.user.service;


import com.user.dto.RegistrationDto;
import com.user.nosql.entity.OrderMetadata;
import com.user.sql.entity.OrderDto;
import com.user.sql.entity.User;

public interface UserService {

	public User registerUser(RegistrationDto registrationDto); 
	public Long saveOrder(OrderDto orderDto); 
}
