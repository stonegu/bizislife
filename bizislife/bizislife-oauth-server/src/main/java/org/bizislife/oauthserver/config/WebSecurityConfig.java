package org.bizislife.oauthserver.config;

import org.bizislife.oauthserver.service.UserAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserAuthServiceImpl userAuthService;
	

//    @Autowired
//    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("john").password("123").roles("USER").and().withUser("tom")
//				.password("111").roles("ADMIN");
//    }

    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
    	
    	auth.userDetailsService(userAuthService).passwordEncoder(new ShaPasswordEncoder());
    	
    }
    
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/login").permitAll().anyRequest().authenticated().and()
				.formLogin().permitAll();
    }

}