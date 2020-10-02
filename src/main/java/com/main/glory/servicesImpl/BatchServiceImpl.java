package com.main.glory.servicesImpl;

import com.main.glory.Dao.BatchDataDao;
import com.main.glory.Dao.BatchGrDetailDao;
import com.main.glory.Dao.BatchMastDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.fabric.FabStockDataDao;
import com.main.glory.Dao.fabric.FabStockMastDao;
import com.main.glory.model.batch.BatchData;
import com.main.glory.model.batch.BatchGrDetail;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.model.fabric.FabStockData;
import com.main.glory.model.fabric.FabStockMast;
import com.main.glory.services.BatchServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service("batchServiceImpl")
public class BatchServiceImpl implements BatchServicesInterface {

    @Autowired
    private BatchMastDao batchMastDao;

    @Autowired
    private BatchDataDao batchDataDao;

    @Autowired
    private BatchGrDetailDao batchGrDetailDao;

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    private FabStockMastDao fabStockMastDao;

    @Autowired
    private FabStockDataDao fabStockDataDao;

    @Transactional
    public void createBatch(BatchMast batchMast) throws Exception{

        for (BatchData e : batchMast.getBatchData()) {
            Boolean isPresent = fabStockDataDao.existsById(e.getFabInId());
            if (!isPresent) {
                throw new Exception("No fabric in of id: " + e.getFabInId());
            }
        }

        for (BatchData e : batchMast.getBatchData()) {
            FabStockData fabStockData = fabStockDataDao.findById(e.getFabInId()).get();
            if(e.getMtr() == fabStockData.getMtr()){
                fabStockData.setIsCut(false);
                fabStockData.setBatchCreated(true);
            } else if(e.getMtr() < fabStockData.getMtr()) {
                fabStockData.setIsCut(true);
                fabStockData.setBatchCreated(true);
                FabStockData newData = new FabStockData(fabStockData);
                newData.setId(null);
                newData.setMtr(fabStockData.getMtr() - e.getMtr());
                fabStockDataDao.save(newData);
            }
        }
        BatchMast batchMast1 = batchMastDao.save(batchMast);

    }

}

