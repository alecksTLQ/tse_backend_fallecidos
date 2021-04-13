package com.example.springsocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.springsocial.config.AppProperties;

@SpringBootApplication
@Import(SwaggerConfiguration.class)
@EnableConfigurationProperties(AppProperties.class)
@EnableTransactionManagement
public class SpringSocialApplication extends SpringBootServletInitializer implements WebMvcConfigurer{


	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringSocialApplication.class);
    }

	
	public static void main(String[] args) {
		SpringApplication.run(SpringSocialApplication.class, args);
	}
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
	}
	
}
