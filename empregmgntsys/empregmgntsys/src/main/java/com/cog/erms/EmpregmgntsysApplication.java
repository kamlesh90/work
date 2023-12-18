package com.cog.erms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.Servers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(
		info = @Info(
			title = "EMPLOYEE REG API",
				version = "1.0.0",
				description = "EMPLOYEE REG API DOCUMENT"
		),
		servers = @Server(
			url = "http://localhost:8080",
				description = "employee reg api url "
		)
)
public class EmpregmgntsysApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmpregmgntsysApplication.class, args);
	}

}
