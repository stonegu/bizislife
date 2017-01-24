package org.bizislife.oauthserver.config;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.yaml.SpringProfileDocumentMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

@Configuration
public class PropertiesLoaderConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesLoaderConfiguration.class);

	@Value("${spring.profiles.active}")
	private String profileActive;

	@Value("${properties.name.app}")
	private String propertiesNameApp;

	@Value("${properties.name.jdbc}")
	private String propertiesNameJdbc;
	
	/*	
	 * In order to resolve ${...} placeholders in definitions or @Value annotations using properties from a PropertySource, 
	 * one must register a PropertySourcesPlaceholderConfigurer. This happens automatically when using in XML, 
	 * but must be explicitly registered using a static @Bean method when using @Configuration classes.
	 * 
	 * All the properties (yaml) files will be read and processed before any beans get created, because PropertySourcesPlaceholderConfigurer
	 * implements BeanFactoryPostProcessor!!!
	*/	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {

		logger.info("****** get all properties files: application.yml, app-activeProfile.yml, jdbc-activeProfile.yml");

		List<Properties> propList = new ArrayList<>();
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();

		// get app's default properties file : application.yml
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(getResource("application.yml"));
		SpringProfileDocumentMatcher profilematch = new SpringProfileDocumentMatcher();
		profilematch.addActiveProfiles(yaml.getObject().getProperty("spring.profiles.active"));
		yaml.setDocumentMatchers(profilematch);
		propList.add(yaml.getObject());

		// get app's other properties file : app-activeProfile.yml &
		// jdbc-activeProfile.yml (based on active profile)
		String[] properties_names = { yaml.getObject().getProperty("properties.name.app"),
				yaml.getObject().getProperty("properties.name.jdbc") };
		for (String pname : properties_names) {
			Resource res = getResource(pname);
			if (res != null && res.exists()) {
				
				if (res.getFilename().endsWith(".yml")) {
					YamlPropertiesFactoryBean yaml_temp = new YamlPropertiesFactoryBean();
					yaml_temp.setResources(res);
					propList.add(yaml_temp.getObject());
				} else if (res.getFilename().endsWith(".properties")) {
					try {
						Properties prop = new Properties();
						prop.load(res.getInputStream());
						propList.add(prop);
					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			}
		}

		ppc.setPropertiesArray(propList.toArray(new Properties[propList.size()]));

		return ppc;
	}

	@Bean
	public boolean propertiesLogs() {

		logger.info("****** spring.profiles.active: " + profileActive);
		logger.info("****** properties.name.app: " + propertiesNameApp);
		logger.info("****** properties.name.jdbc: " + propertiesNameJdbc);

		return true;
	}

	public static Resource getResource(final String filename) {
		Resource resource = null;
		// priority from
		// 1. /config
		// 2. /resources
		// 2. /
		// 3. classPath
		final Resource[] possiblePropertiesResources = { new PathResource("config/" + filename),
				new PathResource("resources/" + filename), new PathResource(filename),
				new ClassPathResource(filename) };

		for (Resource res : possiblePropertiesResources) {
			if (res.exists()) {
				try {
					logger.info("****** find properties resouce: " + res.getURL().toString());
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
				resource = res;
				break;
			}
		}
		return resource;
	}

}