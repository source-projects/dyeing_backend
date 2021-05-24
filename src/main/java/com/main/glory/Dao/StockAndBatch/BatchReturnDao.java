package com.main.glory.Dao.StockAndBatch;


import com.main.glory.model.StockDataBatchData.BatchReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BatchReturnDao extends JpaRepository<BatchReturn,Long> {

    @Query(value = "select max(x.chl_no) from batch_return as x LIMIT 1",nativeQuery = true)
    Long getlatestChlNo();
}
