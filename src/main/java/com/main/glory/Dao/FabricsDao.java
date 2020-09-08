package com.main.glory.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.main.glory.model.Fabric;


@EnableJpaRepositories
public interface FabricsDao extends JpaRepository<Fabric, Long> {

}
