package com.user.profile.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.user.profile.UserProfile;
import com.user.sql.entity.User;
import com.user.systems.CRMSystem;
import com.user.systems.VendorSystem1;
import com.user.systems.VendorSystem2;
import com.user.util.Util;

public class VendorUserProfile implements UserProfile{

	@Autowired
	VendorSystem1 vendorSystem1;
	@Autowired
	VendorSystem2 vendorSystem2;
	@Autowired
	CRMSystem crmSystem;
	
	@Override
	public void sendWelcomeEmail(User user) {
		// Implement logic to retrieve welcome email from a data source or html file path
		Util.sendEmail(user.getEmail(), "staff@ums.com","Welcome Vendor to our platform! Your profile is ready for use.");
	}

	@Override
	public void integrateThirdPartySystems(User user) {
		// TODO Auto-generated method stub
		vendorSystem1.registerUser(user);
		vendorSystem2.registerUser(user);
			
	}

	@Override
	public void syncCRM(User user) {

		// Sync to CRM
		crmSystem.syncUser(user);
		
	}

}
