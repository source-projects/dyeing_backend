package com.main.glory.Dao.StockAndBatch;

import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.quality.Quality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockMastDao extends JpaRepository<StockMast, Long> {

 @Query(value = "Select * from com.main.glory.model.StockDataBatchData.StockMast s where s.qualityId.id = id",nativeQuery = true)
 StockMast findByQualityId(@Param("id") Long qualityId);
}
