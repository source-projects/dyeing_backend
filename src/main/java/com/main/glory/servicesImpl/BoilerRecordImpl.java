package com.main.glory.servicesImpl;

import com.main.glory.Dao.machine.BoilerMachineRecordDao;
import com.main.glory.model.machine.BoilerMachineRecord;
import com.main.glory.model.machine.MachineMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("boilerServiceImpl")
public class BoilerRecordImpl {


    @Autowired
    MachineServiceImpl machineService;

    @Autowired
    BoilerMachineRecordDao boilerMachineRecordDao;

    public void saveMachine(List<BoilerMachineRecord> boilerMachineRecordList) throws Exception {


        List<BoilerMachineRecord> list=new ArrayList<>();
        for(BoilerMachineRecord boilerMachineRecord:boilerMachineRecordList)
        {
            //check the machine is exist or not
            MachineMast machineMastExist = machineService.getMachineByMachineId(boilerMachineRecord.getControlId());

            list.add(boilerMachineRecord);

        }

        boilerMachineRecordDao.saveAll(list);

    }

    public List<BoilerMachineRecord> getAllMachineRecord() throws Exception{

        List<BoilerMachineRecord> boilerMachineRecords = boilerMachineRecordDao.findAll();
        if(boilerMachineRecords.isEmpty())
            throw new Exception("no data found");

        return boilerMachineRecords;
    }
}
