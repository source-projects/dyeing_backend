package com.main.glory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan({"com.main.glory.controller","com.main.glory.servicesImpl","com.main.glory.config"})
@EnableJpaRepositories({"com.main.glory.Dao"})
@SpringBootApplication
public class GloryautofabApplication {
	public static void main(String[] args) {
		SpringApplication.run(GloryautofabApplication.class, args);
	}

}
