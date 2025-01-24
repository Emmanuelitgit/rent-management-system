package com.rent_management_system;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "Rent Management API", version = "v1", description = "API documentation for Rent Management System"))
public class RentManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentManagementSystemApplication.class, args);
	}

}
