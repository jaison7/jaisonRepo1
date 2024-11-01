package com.user.profile.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.user.profile.UserProfile;
import com.user.sql.entity.User;
import com.user.systems.CRMSystem;
import com.user.util.Util;

public class CustomerUserProfile implements UserProfile{

	@Autowired
	CRMSystem crmSystem;
	
	@Override
	public void sendWelcomeEmail(User user) {
		// Implement logic to retrieve welcome email from a data source or API
		Util.sendEmail(user.getEmail(), "staff@ums.com","Welcome Customer to our platform! Your profile is ready for use.");
	}

	@Override
	public void integrateThirdPartySystems(User user) {
		
		// Add to CRM
		crmSystem.syncUser(user);
		
	}

	@Override
	public void syncCRM(User user) {

		// Sync to CRM
		crmSystem.syncUser(user);
		
		
	}

}

