package com.main.glory.servicesImpl;

import com.main.glory.Dao.dyeingProcess.DyeingProcessMastDao;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dyeingProcessServiceImpl")
public class DyeingProcessServiceImpl {
    @Autowired
    DyeingProcessMastDao dyeingProcessMastDao;

    public void addDyeingProcess(DyeingProcessMast data) {

        dyeingProcessMastDao.save(data);
    }

    public List<DyeingProcessMast> getAllDyeingProcess() throws Exception {

        List<DyeingProcessMast> list = dyeingProcessMastDao.getAllProcess();
        if(list.isEmpty())
            throw new Exception("no data found");

        return list;

    }
}
