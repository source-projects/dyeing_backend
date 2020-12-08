package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.dispatch.DispatchDataDao;
import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("dispatchMastImpl")
public class DispatchMastImpl {

    @Autowired
    DispatchMastDao dispatchMastDao;

    @Autowired
    DispatchDataDao dispatchDataDao;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @Autowired
    BatchDao batchDao;

    public Boolean saveDispatch(DispatchMast dispatchMast) throws Exception {

        //Added the check to check the given info is available or not
        //throwing the serializable error that's why surround the code with try-catch block

        List<BatchData> batchData = batchDao.findByControlIdAndBatchId(dispatchMast.getStockId(),dispatchMast.getBatchId());
        if(batchData.isEmpty())
            throw new Exception("batch not found");

        for(DispatchData dispatchData: dispatchMast.getDispatchData())
        {
            Optional<BatchData> batch = batchDao.findById(dispatchData.getBatchEntryId());

            if(!batch.isPresent())
                throw new Exception("batch gr not found");

            if(batch.get().getFinishMtr()==0)
                throw new Exception("The finish meter is not stored so invoice can't created");

        }

        dispatchMast.getDispatchData().forEach(e->{
            Optional<BatchData> batchData1 = batchDao.findById(e.getBatchEntryId());
            if(batchData1.isPresent())
                batchData1.get().setIsBillGenrated(true);
        });

        dispatchMastDao.save(dispatchMast);
        return true;
    }

    public void getInvoiceById(Long id) {

    }
}
