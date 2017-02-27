package org.bizislife.oauthserver.service;

import java.util.Arrays;

import org.bizislife.oauthserver.dao.UserDao;
import org.bizislife.oauthserver.model.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthServiceImpl implements UserDetailsService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserDao userDao;

	@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDetails userDetails = userDao.getUser(username);
		if (userDetails == null) {
			throw new UsernameNotFoundException("User '" + username + "' not found.");
		}
		
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userDetails.getRole());
		org.springframework.security.core.userdetails.UserDetails details = new User(userDetails.getUsername(), userDetails.getPassword(), Arrays.asList(grantedAuthority));
		
		return details;
	}

}
