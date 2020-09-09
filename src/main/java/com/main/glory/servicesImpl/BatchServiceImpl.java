package com.main.glory.servicesImpl;

import com.main.glory.Dao.BatchMastDao;
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

    @Override
    @Transactional
    public int saveBatch(BatchMast batchMast) throws Exception {
        try {
             batchMastDao.save(batchMast);
             return 0;
        } catch (Exception e){
            return 1;
        }
    }

    @Override
    @Transactional
    public List<BatchMast> getAllBatch() {
        return batchMastDao.findAll();
    }
}
