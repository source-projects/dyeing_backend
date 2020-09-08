package com.main.glory.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.main.glory.model.Quality;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface QualityDao extends JpaRepository<Quality, Long>  {

}
