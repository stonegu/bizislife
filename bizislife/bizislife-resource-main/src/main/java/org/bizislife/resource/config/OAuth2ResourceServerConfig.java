package org.bizislife.resource.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenServices(tokenServices());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
//			.anonymous().disable()
		
			.authorizeRequests()
				.antMatchers("/test/**").permitAll()
				.antMatchers("/test2/**").access("#oauth2.hasScope('foo')")
			;

	}	

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }
	
    @Bean(name = "primaryDataSource")
	@Primary // http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#howto-two-datasources
    @ConfigurationProperties(prefix="sql.jdbc")
    public DataSource primaryDataSource() {
    	logger.info("****** create DataSource for Resource-Main");
        return DataSourceBuilder.create().build();
    }
    
//    @Bean
//    @ConfigurationProperties(prefix="datasource.secondary")
//    public DataSource secondaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }    
    
    
//    @Bean
//    public DataSource dataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(jdbcDriverClassName);
//        dataSource.setUrl(jdbcUrl);
//        dataSource.setUsername(jdbcUsername);
//        dataSource.setPassword(jdbcPassword);
//        return dataSource;
//    }    
    
    
	
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(primaryDataSource());
    }	
}