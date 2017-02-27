package org.bizislife.oauthserver.dao;

import org.bizislife.oauthserver.model.UserDetails;

public interface UserDao {
	public UserDetails getUser(String username);

}
