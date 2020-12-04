package com.main.glory.Dao.jobcard;

import com.main.glory.model.jobcard.JobData;
import com.main.glory.model.jobcard.JobMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface JobMastDao extends JpaRepository<JobMast,Long> {


}
