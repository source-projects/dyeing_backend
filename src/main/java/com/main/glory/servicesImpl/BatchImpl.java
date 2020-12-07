package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("batchImpl")
public class BatchImpl {

    @Autowired
    private BatchDao batchDao;

    @Autowired
    StockMastDao stockMastDao;


    public void saveBatch(BatchData batchData) {

        batchDao.saveAndFlush(batchData);
    }


    public void updateFinishMtrBatch(List<BatchData> batchDataList) throws Exception{

            for (BatchData batch : batchDataList) {
                Optional<BatchData> batchData = batchDao.findById(batch.getId());

                if (batchData.isPresent()) {
                    batchData.get().setId(batch.getId());
                    batchData.get().setIsExtra(batch.getIsExtra());
                    batchData.get().setSequenceId(batch.getId());
                    batchData.get().setFinishMtr(batch.getFinishMtr());
                    batchData.get().setIsBillGenrated(batch.getIsBillGenrated());
                    batchData.get().setIsProductionPlanned(batch.getIsProductionPlanned());
                    batchData.get().setBatchId(batch.getBatchId());
                    batchData.get().setControlId(batch.getControlId());
                    batchDao.save(batchData.get());
                } else if(batch.getId()==0){
                    batchDao.save(batch);
                }
            }

    }

    public List<BatchData> getBatchById(String batchId, Long controlId) throws Exception{
        List<BatchData> batchData = batchDao.findByControlIdAndBatchId(controlId,batchId);

        if(batchData.isEmpty())
            throw new Exception("Batch is not available for batchId:"+batchId);

        return  batchData;
    }

    public List<BatchData> getBatchByDocId(String batchId, Long controlId) {
        return null;
    }
}
