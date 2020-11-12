package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.model.StockDataBatchData.Batch;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.quality.Quality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("batchImpl")
public class BatchImpl {

    @Autowired
    private BatchDao batchDao;

    @Autowired
    StockMastDao stockMastDao;


    public void saveBatch(Batch batch) {

        batchDao.saveAndFlush(batch);
    }

    public List<Batch> getAllStockBatch(Long qualityId) {

        StockMast stock=stockMastDao.findByQualityId(qualityId);
        System.out.print(stock);
        List<Batch> batchList = batchDao.findByControlId(stock.getId());
        System.out.print(batchList);
        return batchList;

    }
}
