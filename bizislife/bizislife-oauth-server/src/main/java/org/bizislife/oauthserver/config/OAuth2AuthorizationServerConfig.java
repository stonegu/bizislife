package org.bizislife.oauthserver.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	@Value("${sql.jdbc.driverClassName}")
//	private String sqlDriverClassName;
//
//	@Value("${sql.jdbc.url}")
//	private String sqlUrl;
//	
//	@Value("${sql.jdbc.username}")
//	private String sqlUsername;
//
//	@Value("${sql.jdbc.password}")
//	private String sqlPassword;
	
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
	
	
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }	
	
    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(primaryDataSource()); 
    }
	
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // @formatter:off
		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));
		endpoints.tokenStore(tokenStore())
				// .accessTokenConverter(accessTokenConverter())
				.tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager);
    }    
    
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
    
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
	
    
    
    @Bean(name = "primaryDataSource")
	@Primary // http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#howto-two-datasources
    @ConfigurationProperties(prefix="sql.jdbc")
    public DataSource primaryDataSource() {
    	logger.info("****** create DataSource for Oauth2-Server");
        return DataSourceBuilder.create().build();
    }
    
//    @Bean
//    public DataSource primaryDataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(sqlDriverClassName);
//        dataSource.setUrl(sqlUrl);
//        dataSource.setUsername(sqlUsername);
//        dataSource.setPassword(sqlPassword);
//        return dataSource;
//    }    
    
//    @Bean
//    @ConfigurationProperties(prefix="datasource.secondary")
//    public DataSource secondaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }    

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(primaryDataSource());
    }
    
}