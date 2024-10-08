package com.grocery_hub.grocery_hub;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@OpenAPIDefinition
public class GroceryHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroceryHubApplication.class, args);
	}

}
