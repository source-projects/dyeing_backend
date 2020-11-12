package com.main.glory.Dao.StockAndBatch;


import com.main.glory.model.StockDataBatchData.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchDao extends  JpaRepository<Batch, Long> {


    List<Batch> findByControlId(Long Id);
}

