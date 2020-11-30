package com.main.glory.servicesImpl;

import com.main.glory.Dao.machine.MachineCategoryDao;
import com.main.glory.Dao.machine.MachineDao;
import com.main.glory.Dao.machine.MachineRecordDao;
import com.main.glory.model.machine.AddMachineInfo.AddMachineInfo;
import com.main.glory.model.machine.AddMachineInfo.AddMachineRecord;
import com.main.glory.model.machine.MachineCategory;
import com.main.glory.model.machine.MachineMast;
import com.main.glory.model.machine.MachineRecord;
import com.main.glory.model.machine.category.AddCategory;
import com.main.glory.model.machine.response.GetAllCategory;
import com.main.glory.model.machine.response.GetAllMachine;
import com.main.glory.model.machine.response.GetAllMachineRecord;
import com.main.glory.model.machine.response.GetMachineByIdWithFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("machineServiceImpl")
public class MachineServiceImpl {

    @Autowired
    private MachineDao machineDao;

    @Autowired
    private MachineCategoryDao machineCategoryDao;

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

            Optional<MachineCategory> machineCategory = machineCategoryDao.findById(machine.getControlId());
            if(!machineCategory.isPresent())
                throw new Exception("Not such category is available with id:"+machine.getControlId());

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


            List<GetAllMachineRecord> machineRecordList=new ArrayList<>();
            int i=0;
            for(MachineRecord mr : m.getMachineRecords())
            {

                GetAllMachineRecord gr=new GetAllMachineRecord();
                gr.setId(mr.getId());
                gr.setControlId(mr.getControlId());
                gr.setSpeed(mr.getSpeed());
                gr.setCreatedDate(mr.getCreatedDate());
                gr.setUpdatedDate(mr.getUpdatedDate());

                Double mtr = mr.getSpeed()/6;
                gr.setMtr(mtr);

                machineRecordList.add(gr);


            }


            getAllMachine.setGetAllMachineRecords(machineRecordList);

            getAllMachines.add(getAllMachine);
        }
        return getAllMachines;
    }

    public GetAllMachine getMachineById(Long id) throws Exception {

        Optional<MachineMast> machineMast = machineDao.findById(id);
        if(!machineMast.isPresent())
        {
            throw new Exception("No machine found for id:"+id);
        }
        GetAllMachine machine = new GetAllMachine();

        machine.setId(machineMast.get().getId());
        machine.setMachineName(machineMast.get().getMachineName());

        List<GetAllMachineRecord> getAllMachineRecordList = new ArrayList<>();
        for(MachineRecord machineRecord : machineMast.get().getMachineRecords())
        {
            GetAllMachineRecord getAllMachineRecord=new GetAllMachineRecord();
            getAllMachineRecord.setId(machineRecord.getId());
            getAllMachineRecord.setControlId(machineRecord.getControlId());
            getAllMachineRecord.setSpeed(machineRecord.getSpeed());
            getAllMachineRecord.setUpdatedDate(machineRecord.getUpdatedDate());
            getAllMachineRecord.setCreatedDate(machineRecord.getCreatedDate());

            Double mtr = machineRecord.getSpeed()/6;
            getAllMachineRecord.setMtr(mtr);
            getAllMachineRecordList.add(getAllMachineRecord);

        }


        machine.setGetAllMachineRecords(getAllMachineRecordList);



        return machine;

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

    public void saveMachineCategory(AddCategory machine) throws Exception{

        MachineCategory machineCategory=new MachineCategory();
        machineCategory.setName(machine.getName());
        machineCategoryDao.save(machineCategory);
    }

    public List<GetAllCategory> getAllCategory() {

        List<GetAllCategory> getAllCategoryList = new ArrayList<>();
        List<MachineCategory> machineCategories =  machineCategoryDao.findAll();
        for(MachineCategory m:machineCategories)
        {
            GetAllCategory category=new GetAllCategory();
            category.setId(m.getId());
            category.setName(m.getName());

            getAllCategoryList.add(category);

        }
        return getAllCategoryList;

    }

    public List<GetAllMachine> getAllMachineByCategory(Long id) throws Exception{

        Optional<MachineCategory> machineCategory = machineCategoryDao.findById(id);
        if(!machineCategory.isPresent())
            throw new Exception("category not found with id:"+id);

        List<GetAllMachine> getAllMachineList = new ArrayList<>();
        List<MachineMast> machineMast = machineDao.findByControlId(id);
        for(MachineMast mm:machineMast)
        {
            GetAllMachine machine=new GetAllMachine();
            machine.setId(mm.getId());
            machine.setMachineName(mm.getMachineName());

            List<GetAllMachineRecord> machineRecordList=new ArrayList<>();
            int i=0;
            for(MachineRecord mr : mm.getMachineRecords())
            {

                GetAllMachineRecord gr=new GetAllMachineRecord();
                gr.setId(mr.getId());
                gr.setControlId(mr.getControlId());
                gr.setSpeed(mr.getSpeed());
                gr.setCreatedDate(mr.getCreatedDate());
                gr.setUpdatedDate(mr.getUpdatedDate());

                Double mtr = mr.getSpeed()/6;
                gr.setMtr(mtr);

                machineRecordList.add(gr);


            }
            machine.setGetAllMachineRecords(machineRecordList);
            getAllMachineList.add(machine);

        }
        return getAllMachineList;


    }

    public GetAllMachine getMachineByIdWithFilter(GetMachineByIdWithFilter getMachine) throws Exception{

        SimpleDateFormat formatter ;

        Date d1=null;
        Date d1Time=null;
        Date d2Time=null;
        Date d2=null;

        GetAllMachine getAllMachine;
        Optional<MachineMast> machineMast = machineDao.findById(getMachine.getId());

        if(!machineMast.isPresent())
            throw new Exception("Not data found for id:"+getMachine.getId());


        if(getMachine.getFromDate()!=null && getMachine.getToDate()!=null)
        {
            getAllMachine = new GetAllMachine();
            getAllMachine.setId(machineMast.get().getId());
            getAllMachine.setMachineName(machineMast.get().getMachineName());

            List<GetAllMachineRecord> getAllMachineRecordList = new ArrayList<>();

            for(MachineRecord machineRecord : machineMast.get().getMachineRecords())
            {
                GetAllMachineRecord getAllMachineRecord=new GetAllMachineRecord(machineRecord);

                //given fromDate
                formatter =  new SimpleDateFormat("yyyy-MM-dd");
                d1=formatter.parse(getMachine.getFromDate());

                //given to date
                d2=formatter.parse(getMachine.getToDate());

                //machine record date
                Date date = formatter.parse(getAllMachineRecord.getCreatedDate().toString());
                //compare
                if(!date.before(d1) && !date.after(d2))
                {
                    getAllMachineRecordList.add(getAllMachineRecord);

                }
                System.out.println("Date to check:"+date);
                System.out.println("++"+d1);
                System.out.println("++"+d2);

            }
            getAllMachine.setGetAllMachineRecords(getAllMachineRecordList);

            return getAllMachine;
        }
        /*else if(getMachine.getToTime()!=null && getMachine.getToTime()!=null)
        {
            if(getMachine.getFromDate()!=null && getMachine.getToDate()!=null)
            {
                getAllMachine = new GetAllMachine();
                getAllMachine.setId(machineMast.get().getId());
                getAllMachine.setMachineName(machineMast.get().getMachineName());

                List<GetAllMachineRecord> getAllMachineRecordList = new ArrayList<>();

                for(MachineRecord machineRecord : machineMast.get().getMachineRecords())
                {
                    GetAllMachineRecord getAllMachineRecord=new GetAllMachineRecord(machineRecord);
                    formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC+3"));
                    System.out.println("Date to check 1 :"+formatter.format(machineRecord.getCreatedDate()));



                }
                getAllMachine.setGetAllMachineRecords(getAllMachineRecordList);

                return getAllMachine;
            }

        }*/


        return null;

    }
}
