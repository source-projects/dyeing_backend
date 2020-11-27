package com.main.glory.servicesImpl;

import com.main.glory.Dao.machine.MachineDao;
import com.main.glory.Dao.machine.MachineRecordDao;
import com.main.glory.model.machine.AddMachineInfo.AddMachineInfo;
import com.main.glory.model.machine.AddMachineInfo.AddMachineRecord;
import com.main.glory.model.machine.MachineMast;
import com.main.glory.model.machine.MachineRecord;
import com.main.glory.model.machine.response.GetAllMachine;
import com.main.glory.model.program.Program;
import org.aspectj.weaver.patterns.ExactAnnotationFieldTypePattern;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("machineServiceImpl")
public class MachineServiceImpl {

    @Autowired
    private MachineDao machineDao;

    @Autowired
    private MachineRecordDao machineRecordDao;


    @Autowired
    ModelMapper modelMapper;



    public void saveMachine(AddMachineInfo machine) throws Exception{

            modelMapper.getConfiguration().setAmbiguityIgnored(true);

            MachineMast machineMast = modelMapper.map(machine, MachineMast.class);

            Optional<MachineMast> machineData =machineDao.findByMachineName(machineMast.getMachineName());

            if(machineData.isPresent())
            {
                throw new Exception("Machine Data is already Exits for machine name:"+machine.getMachineName());
            }

            machineDao.save(machineMast);


        }


    public void saveMachineRecord(AddMachineRecord machine) {


    }

    public void saveMachineRecord(String name, Double speed) throws Exception{

        Optional<MachineMast> machineMast = machineDao.findByMachineName(name);

        if(!machineMast.isPresent())
        {
            throw new Exception("No such machine found with name:"+name);

        }

        MachineRecord machineRecord =  new MachineRecord();
        machineRecord.setControlId(machineMast.get().getId());
        machineRecord.setSpeed(speed);
        machineRecord.setControlId(machineMast.get().getId());

        machineRecordDao.save(machineRecord);


    }

    public List<GetAllMachine> getAllMachine() {

        List<GetAllMachine> getAllMachines=new ArrayList<>();
        List<MachineMast> machineMasts=machineDao.findAll();

        for(MachineMast m:machineMasts)
        {
            GetAllMachine getAllMachine=new GetAllMachine();
            getAllMachine.setId(m.getId());
            getAllMachine.setMachineName(m.getMachineName());
            getAllMachines.add(getAllMachine);
        }
        return getAllMachines;
    }

    public MachineMast getMachineById(Long id) throws Exception {

        Optional<MachineMast> machineMast = machineDao.findById(id);
        if(!machineMast.isPresent())
        {
            throw new Exception("No machine found for id:"+id);
        }

        return machineMast.get();

    }

    public boolean deleteMachineById(Long id) throws Exception {

        Optional<MachineMast> machineMast = machineDao.findById(id);
        if(!machineMast.isPresent())
        {
            throw new Exception("No machine found for id:"+id);
        }
        machineDao.deleteById(machineMast.get().getId());
        return true;
    }
}
