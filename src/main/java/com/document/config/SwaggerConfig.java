package com.document.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Document Upload API").version("3.0.0")
				.description("API Documentation For Upload Document"));
	}
}
