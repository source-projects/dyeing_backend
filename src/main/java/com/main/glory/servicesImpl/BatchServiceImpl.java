package com.main.glory.servicesImpl;

import com.main.glory.Dao.BatchDataDao;
import com.main.glory.Dao.BatchGrDetailDao;
import com.main.glory.Dao.BatchMastDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.fabric.FabStockMastDao;
import com.main.glory.model.batch.BatchGrDetail;
import com.main.glory.model.batch.BatchMast;
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

    void createBatch(BatchMast batchMast){
        BatchMast batchMast1 = batchMastDao.save(batchMast);

        batchMast.getBatchData().forEach(e -> {
//            Boolean fabStockMastDao.existsById(e.getFabDataId());

        });
    }

}

