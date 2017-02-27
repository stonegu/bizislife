package org.bizislife.oauthserver.dao;

import org.bizislife.oauthserver.model.UserDetails;
import org.bizislife.oauthserver.model.rowMapper.UserDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public UserDetails getUser(String username) {
		try {
			final String sql = "SELECT u.username username, u.password PASSWORD, ur.role role FROM users u, user_roles ur WHERE u.username = ? AND u.userid = ur.userid";
			UserDetails userDetails = jdbcTemplate.queryForObject(sql, new Object [] {username}, new UserDetailsMapper());
			return userDetails;
		} catch (EmptyResultDataAccessException ex) {
			logger.info(ex.getMessage());
			return null;
		}
	}

}
