package com.main.glory;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;


@ComponentScan({"com.main.glory.controller", "com.main.glory.PaginationModel", "com.main.glory.servicesImpl", "com.main.glory.config", "com.main.glory.filters", "com.main.glory.utils", "com.main.glory.*"})
@EnableJpaRepositories({"com.main.glory.Dao"})
@SpringBootApplication
@EnableTransactionManagement
public class GloryautofabApplication {
    public static void main(String[] args) {
        SpringApplication.run(GloryautofabApplication.class, args);
    }
}
