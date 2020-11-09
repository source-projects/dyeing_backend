package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.model.StockDataBatchData.Batch;
import com.main.glory.model.StockDataBatchData.StockMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("stockBatchServiceImpl")
public class StockBatchServiceImpl {

    @Autowired
    StockMastDao stockMastDao;

    @Autowired
    BatchDao batchDao;

    public void createStockBatch(StockMast stockMast, Batch batch) {
        if(stockMast!=null)
        {
            StockMast data = stockMastDao.saveAndFlush(stockMast);
            batchDao.saveAndFlush(batch);
        }

    }
}
