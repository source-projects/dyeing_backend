package com.main.glory.servicesImpl;

import com.main.glory.Dao.dyeingProcess.DyeingProcessMastDao;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.dyeingProcess.request.GetAllDyeingProcessList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("dyeingProcessServiceImpl")
public class DyeingProcessServiceImpl {
    @Autowired
    DyeingProcessMastDao dyeingProcessMastDao;

    public void addDyeingProcess(DyeingProcessMast data) {

        dyeingProcessMastDao.save(data);
    }

    public List<GetAllDyeingProcessList> getAllDyeingProcess() throws Exception {

        List<GetAllDyeingProcessList> list = new ArrayList<>();
        List<DyeingProcessMast> processList = dyeingProcessMastDao.getAllProcess();
        if(processList.isEmpty())
            throw new Exception("no data found");

        for(DyeingProcessMast d:processList)
        {
            list.add(new GetAllDyeingProcessList(d));
        }

        return list;

    }

    public DyeingProcessMast getDyeingProcessById(Long processId) throws Exception {

        DyeingProcessMast x = dyeingProcessMastDao.getDyeingProcessById(processId);
        if(x==null)
            throw new Exception("no process data found");

        return x;
    }
}
