package com.main.glory.Dao.admin;

import com.main.glory.model.admin.BatchSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BatchSequneceDao extends JpaRepository<BatchSequence,Long> {


    @Query(value = "select * from batch_sequence as x LIMIT 1",nativeQuery = true)
    BatchSequence getBatchSequence();

    @Query("select x from BatchSequence x where x.id=:id")
    BatchSequence getBatchSequenceById(Long id);
}
