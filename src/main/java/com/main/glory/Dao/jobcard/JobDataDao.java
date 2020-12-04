package com.main.glory.Dao.jobcard;

import com.main.glory.model.jobcard.JobData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface JobDataDao extends JpaRepository<JobData,Long> {

    Optional<JobData> findByBatchEntryId(Long id);
}
