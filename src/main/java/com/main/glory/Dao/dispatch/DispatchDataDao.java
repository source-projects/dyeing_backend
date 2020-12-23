package com.main.glory.Dao.dispatch;

import com.main.glory.model.dispatch.DispatchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DispatchDataDao extends JpaRepository<DispatchData, Long> {


    List<DispatchData> findByControlId(Long id);


    @Transactional
    void deleteByBatchEntryId(Long key);
}
