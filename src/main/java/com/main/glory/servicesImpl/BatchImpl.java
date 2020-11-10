package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.model.StockDataBatchData.Batch;
import com.main.glory.model.StockDataBatchData.StockMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("batchImpl")
public class BatchImpl {

    @Autowired
    private BatchDao batchDao;


    public void saveBatch(Batch batch) {

        batchDao.saveAndFlush(batch);
    }
}
