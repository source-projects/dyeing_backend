package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.AddStockBatch;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;

import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("testingServicecimpl")
@Transactional
public class TestingServiceImpl {

    @Autowired
    StockMastDao stockMastDao;

    @Autowired
    BatchDao batchDao;

    @Autowired
    PartyDao partyDao;

    @Autowired
    QualityDao qualityDao;




    public void saveStockMast(AddStockBatch stockMast) throws Exception {
        Party party=partyDao.findById(stockMast.getPartyId()).get();
        Quality quality=qualityDao.findById(stockMast.getQualityId()).get();
        StockMast data = new StockMast(stockMast,party,quality);

        StockMast x = stockMastDao.save(data);

        x.setBatchData(stockMast.getBatchData());
        saveBatchData(x);


    }



    private void saveBatchData(StockMast x) throws Exception {
        List<BatchData> batchDataList = new ArrayList<>();
        for(BatchData batchData:x.getBatchData())
        {
            BatchData batchDataExistWithName = batchDao.getIsBatchId(batchData.getBatchId());

            if(batchDataExistWithName!=null)
                throw new Exception("batch id exist");

            batchData.setControlId(x.getId());
            batchDataList.add(batchData);
        }

        batchDao.saveAll(batchDataList);
    }

    /*public void updateBatchList(List<BatchData> list) {
        BatchData batchData = list.get(0);
        batchData.setMtr(100.0);
        list.set(0,batchData);
    }

    public void updateBatchList(BatchData batchData) {
        batchData.setMtr(1000.0);
    }*/
}
