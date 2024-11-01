package com.user.profile.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.user.profile.UserProfile;
import com.user.sql.entity.User;
import com.user.systems.CRMSystem;
import com.user.systems.HRSystem;
import com.user.systems.VendorSystem1;
import com.user.systems.VendorSystem2;
import com.user.util.Util;

public class AdminUserProfile implements UserProfile{

	@Autowired
	HRSystem hrSystem;
	@Autowired
	CRMSystem crmSystem;
	@Autowired
	VendorSystem1 vendorSystem1;
	@Autowired
	VendorSystem2 vendorSystem2;
	
	@Override
	public void sendWelcomeEmail(User user) {
		// Implement logic to retrieve welcome email from a data source or API
		Util.sendEmail(user.getEmail(), "staff@ums.com","Welcome Customer to our platform! Your profile is ready for use.");
	}

	@Override
	public void integrateThirdPartySystems(User user) {

		//Create user in HRMS
		hrSystem.register(user);
		// Add to CRM
		crmSystem.syncUser(user);
	}

	@Override
	public void syncCRM(User user) {
		// Sync to CRM
		crmSystem.syncUser(user);
		
	}

}
