package com.main.glory.Dao.jobcard;

import com.main.glory.model.jobcard.JobData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface JobDataDao extends JpaRepository<JobData,Long> {

}
