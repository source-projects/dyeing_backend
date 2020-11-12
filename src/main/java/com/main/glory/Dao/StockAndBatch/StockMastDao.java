package com.main.glory.Dao.StockAndBatch;

import com.main.glory.model.StockDataBatchData.StockMast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMastDao extends JpaRepository<StockMast, Long> {

 StockMast findByQualityId(String qualityId);
}
