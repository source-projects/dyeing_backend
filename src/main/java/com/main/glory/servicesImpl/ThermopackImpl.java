package com.main.glory.servicesImpl;

import com.main.glory.Dao.machine.ThermopackDao;
import com.main.glory.model.machine.MachineMast;
import com.main.glory.model.machine.Thermopack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("thermopackImpl")
public class ThermopackImpl {

    @Autowired
    ThermopackDao thermopackDao;

    @Autowired
    MachineServiceImpl machineService;

    public void saveThermopackRecord(Thermopack thermopackRecord) throws Exception {

        MachineMast machineMastExist= machineService.getMachineByMachineId(thermopackRecord.getControlId());

        thermopackDao.save(thermopackRecord);
    }

    public List<Thermopack> getAllMachineRecord() throws Exception{

        List<Thermopack> thermopackList = thermopackDao.findAll();
        if(thermopackList.isEmpty())
            throw new Exception("no data found");

        return thermopackList;

    }
}
