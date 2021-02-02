package com.main.glory.servicesImpl;

import com.main.glory.Dao.dyeingSlip.DyeingSlipMastDao;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dyeingSlipServiceImpl")
public class DyeingSlipServiceImpl {

    @Autowired
    DyeingSlipMastDao dyeingSlipMastDao;

    public void saveDyeingSlipMast(DyeingSlipMast dyeingSlipMast)
    {
        dyeingSlipMastDao.save(dyeingSlipMast);
    }

    public void updateDyeingSlip(DyeingSlipMast data) throws Exception {
        DyeingSlipMast dyeingSlipMast = dyeingSlipMastDao.getDyeingSlipById(data.getId());
        if(dyeingSlipMast==null)
            throw new Exception("no slip data found");

        dyeingSlipMastDao.saveAndFlush(data);

    }

    public DyeingSlipMast getDyeingSlipByBatchStockId(String batchId, Long stockId) {
        DyeingSlipMast dyeingSlipMastExist = dyeingSlipMastDao.findByBatchIdAndStockId(batchId, stockId);
        return  dyeingSlipMastExist;
    }

    public List<DyeingSlipMast> getAllDyeingSlip() throws Exception {

        List<DyeingSlipMast> dyeingSlipMast = dyeingSlipMastDao.getAllDyeingSlip();
        if(dyeingSlipMast.isEmpty())
            throw new Exception("no data found");
        return dyeingSlipMast;
    }
}
