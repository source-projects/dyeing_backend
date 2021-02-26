package com.main.glory;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan({"com.main.glory.controller","com.main.glory.servicesImpl","com.main.glory.config","com.main.glory.filters","com.main.glory.utils","com.main.glory.*"})
@EnableJpaRepositories({"com.main.glory.Dao"})
@OpenAPIDefinition(info = @Info(title = "Turf Booking", version = "0.1", description = "API documentation of turf booking project."))
@SpringBootApplication
public class GloryautofabApplication {
	public static void main(String[] args) {
		SpringApplication.run(GloryautofabApplication.class, args);
	}
}
