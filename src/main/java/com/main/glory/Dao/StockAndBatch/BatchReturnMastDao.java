package com.main.glory.Dao.StockAndBatch;

import com.main.glory.model.StockDataBatchData.BatchReturn;
import com.main.glory.model.StockDataBatchData.BatchReturnMast;
import com.main.glory.model.StockDataBatchData.response.BatchReturnResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BatchReturnMastDao extends JpaRepository<BatchReturnMast,Long> {

    @Query(value = "select max(x.chl_no) from batch_return_mast as x LIMIT 1",nativeQuery = true)
    Long getlatestChlNo();

    @Query(value = "select * from batch_return_mast as x where x.chl_no =:chlNo LIMIT 1",nativeQuery = true)
    BatchReturnMast getChalNoExist(@Param("chlNo") Long chlNo);

    @Query("select x from BatchReturnMast x")
    List<BatchReturnMast> getAllBatchReturn();
}
