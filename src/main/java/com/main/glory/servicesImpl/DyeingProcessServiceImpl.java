package com.main.glory.servicesImpl;

import com.main.glory.Dao.dyeingProcess.DyeingChemicalDataDao;
import com.main.glory.Dao.dyeingProcess.DyeingProcessDataDao;
import com.main.glory.Dao.dyeingProcess.DyeingProcessMastDao;
import com.main.glory.model.dyeingProcess.DyeingChemicalData;
import com.main.glory.model.dyeingProcess.DyeingProcessData;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.dyeingProcess.request.GetAllDyeingProcessList;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.qualityProcess.Chemical;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.supplier.SupplierRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("dyeingProcessServiceImpl")
public class DyeingProcessServiceImpl {

    @Autowired
    SupplierServiceImpl supplierService;

    @Autowired
    ShadeServiceImpl shadeService;
    @Autowired
    DyeingChemicalDataDao dyeingChemicalDataDao;

    @Autowired
    DyeingProcessDataDao dyeingProcessDataDao;

    @Autowired
    DyeingProcessMastDao dyeingProcessMastDao;

    public void addDyeingProcess(DyeingProcessMast data) throws Exception {
        //check the dyeing process already exist with the name or not
        DyeingProcessMast dyeingProcessMastExistWithName = dyeingProcessMastDao.getDyeingProcessByName(data.getProcessName());
        if(dyeingProcessMastExistWithName!=null)
            throw new Exception("already process exist with name");
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
        int i=0;
        List<DyeingProcessData> dyeingProcessDataList = new ArrayList<>();
        for(DyeingProcessData dyeingProcessData:x.getDyeingProcessData())
        {
            List<DyeingChemicalData> dyeingChemicalDataList = new ArrayList<>();

            for(DyeingChemicalData dyeingChemicalData:dyeingProcessData.getDyeingChemicalData())
            {
                SupplierRate supplierRate = supplierService.getSupplierRateByItemId(dyeingChemicalData.getItemId());
                dyeingChemicalData.setItemName(supplierRate.getItemName());
                dyeingChemicalDataList.add(dyeingChemicalData);
            }

            dyeingProcessData.setDyeingChemicalData(dyeingChemicalDataList);

            dyeingProcessDataList.add(dyeingProcessData);
        }
        x.setDyeingProcessData(dyeingProcessDataList);

        return x;
    }

    public List<DyeingProcessData> getDyeingProcessDataById(Long id) throws Exception {
        List<DyeingProcessData> dyeingProcessDataList = dyeingProcessDataDao.findDyeingProcessDataByControlId(id);
        if(dyeingProcessDataList.isEmpty())
            throw new Exception("no dyeing process data found for id :"+id);
        return dyeingProcessDataList;
    }

    public List<DyeingChemicalData> getChemicalListByDyeingProcessDataId(Long id) {

        List<DyeingChemicalData> list = dyeingChemicalDataDao.getChemicalListByControlId(id);
        return list;
    }

    public void updateDyeingProcess(DyeingProcessMast data) throws Exception {

        DyeingProcessMast dyeingProcessMastExist = dyeingProcessMastDao.getDyeingProcessById(data.getId());

        if(dyeingProcessMastExist==null)
        {
            throw new Exception("no proces found for id:"+data.getId());
        }
        List<ShadeMast> getAllShade= shadeService.getAllShadeByProcessId(dyeingProcessMastExist.getId());
        DyeingProcessMast x = dyeingProcessMastDao.save(data);

        if(!getAllShade.isEmpty()) {
            for (ShadeMast s : getAllShade) {
                shadeService.updateShadeProcessId(s.getId(), x.getId());
            }
        }



    }

    public Boolean deleteByProcessId(Long id) throws Exception {
        Boolean flag=false;
        DyeingProcessMast dyeingProcessMastExist = dyeingProcessMastDao.getDyeingProcessById(id);

        if(dyeingProcessMastExist!=null)
        {
            flag=true;
            //check the process is related to shade or not

            List<ShadeMast> shadeMastList = shadeService.getAllShadeMastByProcessIdForDeleteProcess(dyeingProcessMastExist.getId());
            if(!shadeMastList.isEmpty())
            throw new Exception("remove all the shade first");

            dyeingProcessMastDao.deleteByProcessId(id);
        }
        else
            flag = false;
        return flag;

    }

    public DyeingProcessData getDyeingProcessDataByItemId(Long supplierItemId) {
        return null;
    }

    public List<DyeingChemicalData> getDyeingProcessChemicalDataByItemId(Long id) {
        List<DyeingChemicalData> chemicalList = dyeingChemicalDataDao.getChemicalDataByItemId(id);
        return chemicalList;
    }

    public Boolean dyeingProcessExistWithName(String name, Long id) {
        //if the id is not null then it is inserr request and check in entire db or else/
        //check the record except the given id

        if(id==null) {
            DyeingProcessMast dyeingProcessMast = dyeingProcessMastDao.getDyeingProcessByName(name);
            if (dyeingProcessMast == null)
                return true;
            else
                return false;
        }
        else
        {
            DyeingProcessMast dyeingProcessMast = dyeingProcessMastDao.getDyeingProcessByNameExceptId(name,id);
            if (dyeingProcessMast == null)
                return true;
            else
                return false;
        }
    }

}
