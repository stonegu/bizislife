package org.bizislife.oauthserver.model.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bizislife.oauthserver.model.UserDetails;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author stoneg
 * Spring JDBC Module: mapping Query Results to UserDetails
 *
 */
public class UserDetailsMapper implements RowMapper<UserDetails>{

	@Override
	public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserDetails userDetails = new UserDetails();
		userDetails.setPassword(rs.getString("password"));
		userDetails.setRole(rs.getString("role"));
		userDetails.setUsername(rs.getString("username"));
		
		return userDetails;
	}

}
