package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("batchImpl")
public class BatchImpl {

    @Autowired
    private BatchDao batchDao;

    @Autowired
    StockMastDao stockMastDao;


    public void saveBatch(BatchData batchData) {

        batchDao.saveAndFlush(batchData);
    }

    public List<BatchData> getAllStockBatch(Long qualityId) {

        StockMast stock=stockMastDao.findByQualityId(qualityId);
        System.out.print(stock);
        List<BatchData> batchDataList = batchDao.findByControlId(stock.getId());
        System.out.print(batchDataList);
        return batchDataList;

    }
}
