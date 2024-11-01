package com.user.profile;

import com.user.sql.entity.User;

public interface UserProfile {
	
	public void sendWelcomeEmail(User user);

	public void syncCRM(User user);

	public void integrateThirdPartySystems(User user);

}
