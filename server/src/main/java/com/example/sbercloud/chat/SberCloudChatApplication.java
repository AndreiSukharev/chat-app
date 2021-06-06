package com.example.sbercloud.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SberCloudChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(SberCloudChatApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("/**")
						.allowCredentials(true)
						.allowedHeaders("*")
						.allowedOrigins("*")
						.allowedMethods("*");
			}
		};
	}

}
