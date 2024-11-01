package com.user.service.impl;



import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.dto.RegistrationDto;
import com.user.nosql.entity.OrderMetadata;
import com.user.nosql.repo.OrderMongoRepository;
import com.user.profile.UserProfile;
import com.user.profile.impl.AdminUserProfile;
import com.user.profile.impl.CustomerUserProfile;
import com.user.profile.impl.VendorUserProfile;
import com.user.service.UserService;
import com.user.sql.entity.Order;
import com.user.sql.entity.OrderDto;
import com.user.sql.entity.User;
import com.user.sql.repo.OrderRepository;
import com.user.sql.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderMongoRepository orderMongoRepository;

	@Autowired
	private CrmService crmService; // For CRM integration
	@Autowired
	private ThirdPartyService thirdPartyService; // For third-party provisioning

	@Override
	public User registerUser(RegistrationDto registrationDto) {
		// validation of registrationDto and registration logic
		User mockNewUser = new User();
		User user = userRepository.save(mockNewUser);

		int typeId = Optional.ofNullable(registrationDto)
				.map(RegistrationDto::getUserTypeId)
				.orElseThrow(() -> new RuntimeException("Invalid Registration details"));

		// create user profile based on user type
		UserProfile userProfile;
		if (typeId==1) {
			userProfile = new AdminUserProfile();
		} else if (typeId==2) {
			userProfile = new CustomerUserProfile();
		} else if (typeId==3) {
			userProfile = new VendorUserProfile();
		} else {
			throw new RuntimeException("Invalid user type");
		}

		// Send welcome email as per the type of profile dynamically and asynchronously
		CompletableFuture.runAsync(() -> userProfile.sendWelcomeEmail(user));

		// Provision on third-party platforms asynchronously as per the type
		CompletableFuture.runAsync(() -> userProfile.syncCRM(user));

		// Provision on third-party platforms asynchronously as per the type
		CompletableFuture.runAsync(() -> userProfile.integrateThirdPartySystems(user));

		return user;
	}
	// ... other methods

	@Override
	public Long saveOrder(OrderDto orderDto) {
		OrderMetadata metadata = new OrderMetadata();
		try {

			//validate order details and create newOrder 
			// with a generated order id
			Order mockNewOrder = new Order();
			Order order = orderRepository.save(mockNewOrder);

			orderMongoRepository.save(metadata);
			return metadata.getOrderId();
		}catch(Exception e) {
			//rollback with compensating transactions
			orderRepository.deleteById(metadata.getOrderId());
			orderMongoRepository.deleteById(metadata.getOrderId().toString());
			throw new RuntimeException("Order creation failed");
		}
	}

}
