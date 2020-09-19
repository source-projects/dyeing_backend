package com.main.glory.servicesImpl;

import com.main.glory.Dao.BatchDataDao;
import com.main.glory.Dao.BatchGrDetailDao;
import com.main.glory.Dao.BatchMastDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.model.BatchMast;
import com.main.glory.services.BatchServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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


    @Override
    @Transactional
    public int saveBatch(BatchMast batchMast) throws Exception {
        try {
            var batchMaster= batchMastDao.save(batchMast);
            if(batchMaster!=null)
            {
                batchMaster.getBatchData().forEach(c->{
                    c.setControl_id(batchMaster.getId());
                });
                var batchData=batchDataDao.saveAll(batchMast.getBatchData());
                if(batchData!=null)
                {
                    batchData.forEach(d->{
                        d.getBatchGrDetails().forEach(m->{
                            m.setControl_id(d.getId());
                        });
                        batchGrDetailDao.saveAll(d.getBatchGrDetails());
                    });
                }
            }

             return 1;
        } catch (Exception e){
            return 0;
        }
    }

    @Override
    @Transactional
    public List<BatchMast> getAllBatch() {
        return batchMastDao.findAll();
    }
}
